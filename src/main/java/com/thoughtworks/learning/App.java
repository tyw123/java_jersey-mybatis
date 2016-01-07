package com.thoughtworks.learning;

import com.thoughtworks.learning.core.Allitem;

import com.thoughtworks.learning.core.AllitemRepository;
import com.thoughtworks.learning.core.ForsaveRepository;
import com.thoughtworks.learning.core.ItempRepository;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.session.SqlSessionManager;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {
    //URI用了17行import java.net.URI;
    private static final URI BASE_URI = URI.create("http://localhost:8080/base/");//static	表示在类级别定义，所有实例共享的
    public static final String ROOT_PATH = "helloworld";

    public static void main(String[] args) {//主函数入口都长这样
        try {
            System.out.println("\"Hello World\" Jersey Example App");//打印
            
//            Map<String, String> initParams = new HashMap<>();
//            initParams.put(
//                    ServerProperties.PROVIDER_PACKAGES,
//                    UsersResource.class.getPackage().getType());
            final HttpServer server = GrizzlyHttpServerFactory.createHttpServer(BASE_URI, createSessionInViewConfig(), false);
            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                @Override
                public void run() {
                    server.shutdownNow();
                }
            }));
            server.start();//启动HTTP服务

            System.out.println(String.format("Application started.%nTry out %s%s%nStop the application using CTRL+C",
                    BASE_URI, ROOT_PATH));

            Thread.currentThread().join();
        } catch (IOException | InterruptedException ex) {//try catch异常捕捉，先不管细节
            System.out.println("aa");
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }

    public static ResourceConfig createSessionInViewConfig() throws IOException {
        final ResourceConfig resourceConfig = new ResourceConfig();
        String resource = "mybatis-config.xml";

        final Reader reader  = Resources.getResourceAsReader(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader, "test");
        final SqlSessionManager sqlSessionManager = SqlSessionManager.newInstance(sqlSessionFactory);

        final AllitemRepository AllitemRepository = sqlSessionManager.getMapper(AllitemRepository.class);
        final ForsaveRepository forSaveRepository = sqlSessionManager.getMapper(ForsaveRepository.class);
        final ItempRepository itempRepository = sqlSessionManager.getMapper(ItempRepository.class);

        final ResourceConfig config = new ResourceConfig()
                .packages("com.thoughtworks.learning")
                .register(new AbstractBinder() {
                    @Override
                    protected void configure() {
                        bind(AllitemRepository).to(AllitemRepository.class);
                        bind(forSaveRepository).to(ForsaveRepository.class);
                        bind(itempRepository).to(ItempRepository.class);
                        bind(sqlSessionManager).to(SqlSessionManager.class);
                    }
                });

        return config;
    }


    public static ResourceConfig create() throws IOException {
        final ResourceConfig resourceConfig = new ResourceConfig();
        String resource = "mybatis-config.xml";

        final Reader reader  = Resources.getResourceAsReader(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader, "test");

        SqlSession session = sqlSessionFactory.openSession();
        final AllitemRepository AllitemRepository = session.getMapper(AllitemRepository.class);

        resourceConfig.register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(AllitemRepository).to(AllitemRepository.class);
            }
        }).packages("com.thoughtworks.learning.api");

        return resourceConfig;
    }

}

