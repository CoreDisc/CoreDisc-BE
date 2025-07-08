package com.coredisc.common.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class RedisUtil {

    private final StringRedisTemplate stringRedisTemplate;

    // 주어진 key로 Redis에서 저장된 문자열 값을 조회하여 반환
    public String getData(String key) {
        // Redis에서 문자열 값을 다루는 연산자를 가져옴
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        // 주어진 key로 Redis에서 저장된 문자열 값을 조회해서 반환
        return valueOperations.get(key);
    }

    // Redis에 해당 key가 존재하는지 여부 확인
    public boolean existData(String key) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(key));
    }

    // 만료시간을 설정하여 Redis에 문자열 value를 key로 저장
    // 만료 시간이 지나면 해당 데이터 자동 삭제
    public void setDataExpire(String key, String value, long duration) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        Duration expireDuration = Duration.ofSeconds(duration);
        valueOperations.set(key, value, expireDuration);
    }

    // Redis에서 key에 저장된 데이터 삭제
    public void deleteData(String key) {
        stringRedisTemplate.delete(key);
    }
}
