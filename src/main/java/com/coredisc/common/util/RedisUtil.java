package com.coredisc.common.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class RedisUtil {

    private final RedisTemplate<String, Object> redisTemplate;

    // Redis에 key와 value 저장
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    // 주어진 key로 Redis에서 저장된 문자열 값을 조회하여 반환
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // Redis에 해당 key가 존재하는지 여부 확인
    public boolean exists(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    // 만료시간을 설정하여 Redis에 문자열 value를 key로 저장
    // 만료 시간이 지나면 해당 데이터 자동 삭제
    public void expire(String key, long timeout, TimeUnit unit) {
        redisTemplate.expire(key, timeout, unit);
    }

    // Redis에서 key에 저장된 데이터 삭제
    public boolean delete(String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }
}
