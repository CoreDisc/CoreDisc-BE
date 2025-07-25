package com.coredisc.infrastructure.repository.question.querydsl;

import com.coredisc.presentation.dto.cursor.CursorDTO;
import com.coredisc.presentation.dto.question.QuestionResponseDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class QueryCustomQuestionRepositoryImpl implements QueryCustomQuestionRepository {

    private final EntityManager entityManager;

    @Override
    public CursorDTO<QuestionResponseDTO.BasicQuestionResultDTO> findBasicQuestionListByCategories(
            Long categoryId,
            LocalDateTime cursorCreatedAt,
            String cursorQuestionType,
            Long cursorId,
            int pageSize
    ) {

        String nativeSql =
                "SELECT * FROM (" +
                        "    SELECT p.id AS id, 'PERSONAL' AS question_type, p.content AS question, p.created_at AS created_at " +
                        "    FROM personal_question p " +
                        "    JOIN question_category qc ON qc.personal_question_id = p.id " +
                        "    WHERE qc.category_id = :categoryId " +
                        "  UNION ALL " +
                        "    SELECT o.id AS id, " +
                        "           CASE WHEN o.is_shared = true THEN 'OFFICIAL' ELSE 'DEFAULT' END AS question_type, " +
                        "           o.contents AS question, o.created_at AS created_at " +
                        "    FROM official_question o " +
                        "    JOIN question_category qc ON qc.official_question_id = o.id " +
                        "    WHERE qc.category_id = :categoryId " +
                        ") AS combined " +
                        (cursorCreatedAt != null
                                ? "WHERE (combined.created_at < :cursorCreatedAt) OR " +
                                "(combined.created_at = :cursorCreatedAt AND combined.question_type < :cursorQuestionType) OR " +
                                "(combined.created_at = :cursorCreatedAt AND combined.question_type = :cursorQuestionType AND combined.id < :cursorId) "
                                : "") +
                        "ORDER BY combined.created_at DESC, combined.question_type DESC, combined.id DESC " +
                        "LIMIT :pageSize";

        Query query = entityManager.createNativeQuery(nativeSql);

        query.setParameter("categoryId", categoryId);
        query.setParameter("pageSize", pageSize + 1);

        // (생성날짜, 질문타입, 질문id)를 커서 구분 기준으로
        if (cursorCreatedAt != null) {
            query.setParameter("cursorCreatedAt", Timestamp.valueOf(cursorCreatedAt));
            query.setParameter("cursorQuestionType", cursorQuestionType);
            query.setParameter("cursorId", cursorId);
        }

        @SuppressWarnings("unchecked")
        List<Object[]> questionList = query.getResultList();

        boolean hasNext = questionList.size() > pageSize;

        if (hasNext) {
            questionList = questionList.subList(0, pageSize);
        }

        List<QuestionResponseDTO.BasicQuestionResultDTO> values = questionList.stream()
                .map(row -> new QuestionResponseDTO.BasicQuestionResultDTO(
                        ((Number) row[0]).longValue(),            // id
                        (String) row[1],                          // questionType
                        (String) row[2],                          // question
                        ((Timestamp) row[3]).toLocalDateTime()   // createdAt
                ))
                .toList();

        return new CursorDTO<>(values, hasNext);
    }
}
