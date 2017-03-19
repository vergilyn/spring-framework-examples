package com.vergilyn.springboot.demo.aop;

import com.vergilyn.demo.springboot.aop.AopApplication;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import org.springframework.boot.test.rule.OutputCapture;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author VergiLyn
 * @bolg http://www.cnblogs.com/VergiLyn/
 * @date 2017/3/19
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes=AopApplication.class)
public class AopApplicationTest {
    @Rule
    public OutputCapture outputCapture = new OutputCapture();

    private String profiles;


    @Test
    public void testDefaultSettings() throws Exception {
        AopApplication.main(new String[0]);
        String output = this.outputCapture.toString();
        System.out.println(output);
    }

    @Test
    public void testCommandLineOverrides() throws Exception {
        AopApplication.main(new String[] { "--name=taylor" });
        String output = this.outputCapture.toString();
        assertThat(output).contains("Hello, taylor");
    }
}
