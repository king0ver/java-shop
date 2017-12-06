package com.enation.app.cms.floor.producer;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.AmqpExchange;
import com.enation.app.cms.floor.model.enumeration.CmsManageMsgType;
import com.enation.app.cms.floor.model.vo.CmsManageMsg;
/**
 * 
 * 消息生产者，生产了两种消息 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月13日 下午9:34:18
 */
@Component
public class FloorProducer {
	@Autowired
	private AmqpTemplate amqpTemplate;

	/**
	 * 发送pc端消息
	 * 
	 * @param type 是交换器名称
	 * @param id 路由键
	 * @param operation_type 要发送的对象
	 */
	public void sendPcMessage(CmsManageMsgType type, Integer id, Integer operation_type) {
		this.amqpTemplate.convertAndSend(AmqpExchange.PC_INDEX_CHANGE.name(), "cms-manage-routingKey",
				new CmsManageMsg(id, type, operation_type));
	}

	/**
	 * 发送手机端消息
	 * 
	 * @param type 是交换器名称
	 * @param id 路由键 
	 * @param operation_type 要发送的对象
	 */
	public void sendMobileMessage(CmsManageMsgType type, Integer id, Integer operation_type) {
		this.amqpTemplate.convertAndSend(AmqpExchange.MOBILE_INDEX_CHANGE.name(), "cms-manage-routingKey",
				new CmsManageMsg(id, type, operation_type));
	}
}
