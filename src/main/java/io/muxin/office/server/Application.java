package io.muxin.office.server;

import io.muxin.office.server.config.listener.ReadyListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 主程序
 * Created by muxin on 2017/2/23.
 */
@SpringBootApplication
@EnableScheduling
public class Application {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.addListeners(new ReadyListener());
        app.run(args);
    }

}
