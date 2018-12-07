package com.seagull.beedo;

import com.seagull.beedo.task.ParseTask;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BeedoApplicationTests {

    @Autowired
    private ParseTask executeComponent;
    @Test
    public void contextLoads() {
        /*rrayList<BeedoTaskParseModel> taskParseInfos = new ArrayList<>();
        BeedoTaskParseModel BeedoTaskParseModel = new BeedoTaskParseModel();
        BeedoTaskParseModel.setUid(RandomUtils.getUUID());
        BeedoTaskParseModel.setCron("0/10 * * * * ?");
        BeedoTaskParseModel.setName("测试");
        BeedoTaskParseModel.setTaskStatus(TaskStatusEnum.VALID);
        BeedoTaskParseModel.setThreadCoolSize(4);
        taskParseInfos.add(BeedoTaskParseModel);*/
        executeComponent.exec();
    }

}
