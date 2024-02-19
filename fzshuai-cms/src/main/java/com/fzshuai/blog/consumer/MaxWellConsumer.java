package com.fzshuai.blog.consumer;

import com.alibaba.fastjson.JSON;
import com.fzshuai.blog.domain.Article;
import com.fzshuai.blog.domain.dto.ArticleSearchDTO;
import com.fzshuai.blog.domain.dto.MaxwellDataDTO;
import com.fzshuai.blog.mapper.ElasticsearchMapper;
import com.fzshuai.common.utils.BeanCopyUtils;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.fzshuai.blog.constant.MQConstant.MAXWELL_QUEUE;


/**
 * maxwell监听数据
 *
 * @author fzshuai
 * @date 2024-01-21
 **/
@Component
@RabbitListener(queues = MAXWELL_QUEUE)
public class MaxWellConsumer {

    @Resource
    private ElasticsearchMapper elasticsearchMapper;

    @RabbitHandler
    public void process(byte[] data) {
        // 获取监听信息
        MaxwellDataDTO maxwellDataDTO = JSON.parseObject(new String(data), MaxwellDataDTO.class);
        // 获取文章数据
        Article article = JSON.parseObject(JSON.toJSONString(maxwellDataDTO.getData()), Article.class);
        // 判断操作类型
        switch (maxwellDataDTO.getType()) {
            case "insert":
            case "update":
                // 更新es文章
                elasticsearchMapper.save(BeanCopyUtils.copy(article, ArticleSearchDTO.class));
                break;
            case "delete":
                // 删除文章
                elasticsearchMapper.deleteById(article.getArticleId());
                break;
            default:
                break;
        }
    }

}
