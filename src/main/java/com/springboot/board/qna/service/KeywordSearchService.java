package com.springboot.board.qna.service;

import com.springboot.board.qna.entity.Qna;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Component
public class KeywordSearchService {
    @PersistenceContext(unitName = "test")
    private EntityManager entityManager;

    public List<Qna> searchQna(int pageIndex, int pageSize, String keyword){

        if(entityManager != null){
            SearchSession session = Search.session(entityManager);

            return session.search( Qna.class )
                    .where( f -> f.match().field( "title" ).matching( keyword ).boost( 3 ) )
                    .fetch( pageIndex * pageSize, pageSize ).hits();
        }
        else {
            return new ArrayList<>();
        }
    }


//     FullTextEntityManager는 Hibernate Search 5에서만 사용
//    public void buildSearchIndex() throws InterruptedException {
//        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
//        fullTextEntityManager.createIndexer().startAndWait();
//    }
//
//    public List<Qna> searchQna(String keyword){
//        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
//        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Qna.class).get();
//        Query query = queryBuilder.keyword().wildcard().onField("title")
//                .matching("*" + keyword+"*").createQuery();
//        FullTextQuery fullTextQuery = fullTextEntityManager.createFullTextQuery((org.apache.lucene.search.Query) query, Qna.class);
//
//        return (List<Qna>) fullTextQuery.getResultList();
//    }
}
