package com.wisd.dbs;

import com.wisd.dbs.filters.FilterChain;
import com.wisd.dbs.filters.IPostFilter;
import com.wisd.dbs.filters.IPreFilter;
import com.wisd.dbs.live.LiveCleaner;
import com.wisd.dbs.session.SessionCleaner;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.ExecutorService;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/6/12 10:35
 */
@Component
@AllArgsConstructor
public class PostApplication implements ApplicationRunner {
    private final ApplicationContext applicationContext;
    private final SessionCleaner cleaner;
    private final ExecutorService pool;
    private final LiveCleaner liveCleaner;

    @Override
    public void run(ApplicationArguments args) {
        //加载过滤器
        final val preFilterMap = applicationContext.getBeansOfType(IPreFilter.class);
        final val preFilters = new ArrayList<IPreFilter>(preFilterMap.values());
        preFilters.sort(Comparator.comparing(IPreFilter::getOrder));
        FilterChain.preFilters.addAll(preFilters);
        final val postFilterMap = applicationContext.getBeansOfType(IPostFilter.class);
        final val postFilters = new ArrayList<IPostFilter>(postFilterMap.values());
        postFilters.sort(Comparator.comparing(IPostFilter::getOrder));
        FilterChain.postFilters.addAll(postFilters);
        pool.execute(cleaner::run);
        pool.execute(liveCleaner::run);
    }
}
