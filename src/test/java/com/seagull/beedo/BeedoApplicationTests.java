package com.seagull.beedo;

import com.seagull.beedo.common.enums.TaskStatusEnum;
import com.seagull.beedo.model.TaskParseInfo;
import com.seagull.beedo.task.ParseTask;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import team.seagull.common.base.utils.RandomUtils;

import java.util.ArrayList;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BeedoApplicationTests {

    @Autowired
    private ParseTask executeComponent;
    @Test
    public void contextLoads() {
        ArrayList<TaskParseInfo> taskParseInfos = new ArrayList<>();
        TaskParseInfo TaskParseInfo = new TaskParseInfo();
        TaskParseInfo.setUid(RandomUtils.getUUID());
        TaskParseInfo.setCron("0/10 * * * * ?");
        TaskParseInfo.setName("测试");
        TaskParseInfo.setTaskStatus(TaskStatusEnum.VALID);
        TaskParseInfo.setThreadCoolSize(4);
        taskParseInfos.add(TaskParseInfo);
       // executeComponent.exec(taskParseInfos);
    }

}
