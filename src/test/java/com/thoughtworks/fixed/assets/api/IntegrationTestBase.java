package com.thoughtworks.fixed.assets.api;


import com.thoughtworks.learning.core.AllitemRepository;
import com.thoughtworks.learning.core.ForsaveRepository;
import com.thoughtworks.learning.core.ItempRepository;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.javassist.bytecode.CodeAttribute;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.After;

import javax.ws.rs.core.Application;
import java.io.IOException;
import java.io.Reader;

public class IntegrationTestBase extends JerseyTest {
    private SqlSessionFactory sqlSessionFactory;
    private SqlSession session;
    protected AllitemRepository allitemRepository;

    protected ForsaveRepository forsaveRepository;

    protected ItempRepository itempRepository;

    @Override
    protected Application configure() {
        
//        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);
//        enable(TestProperties.RECORD_LOG_LEVEL);
        String resource = "mybatis-config.xml";

        Reader reader  = null;
        try {
            reader = Resources.getResourceAsReader(resource);


            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);

            session = sqlSessionFactory.openSession();
            session.getConnection().setAutoCommit(false);
            allitemRepository = session.getMapper(AllitemRepository.class);
            forsaveRepository = session.getMapper(ForsaveRepository.class);
            itempRepository = session.getMapper(ItempRepository.class);

            return new ResourceConfig().register(new AbstractBinder() {


                @Override
                protected void configure() {

                    bind(allitemRepository).to(AllitemRepository.class);
                    bind(forsaveRepository).to(ForsaveRepository.class);
                    bind(itempRepository).to(ItempRepository.class);


                }
            }).packages("com.thoughtworks.learning.api");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @After
    public void tearDown(){
        session.rollback();
        session.close();

    }
}
