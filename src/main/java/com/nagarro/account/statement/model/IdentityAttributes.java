package com.nagarro.account.statement.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@RedisHash("IdentityAttributes")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IdentityAttributes {

    @Id
    private String authId;
    private String token;
    @TimeToLive(unit = TimeUnit.MINUTES)
    private Long validity;

}
