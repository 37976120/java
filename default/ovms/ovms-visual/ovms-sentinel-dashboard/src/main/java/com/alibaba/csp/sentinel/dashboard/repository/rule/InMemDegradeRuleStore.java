
package com.alibaba.csp.sentinel.dashboard.repository.rule;

import java.util.concurrent.atomic.AtomicLong;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.DegradeRuleEntity;

import org.springframework.stereotype.Component;

/**
 * @author leyou
 */
@Component
public class InMemDegradeRuleStore extends InMemoryRuleRepositoryAdapter<DegradeRuleEntity> {

    private static AtomicLong ids = new AtomicLong(0);

    @Override
    protected long nextId() {
        return ids.incrementAndGet();
    }
}
