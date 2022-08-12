package com.vergilyn.examples.springboot.usage.u0004;

import org.springframework.stereotype.Component;

@Component
public class WechatStatsChain implements StatsChain {

	private String message = "{\"callsName\":\"OWL\",\"dingtalkGroupParams\":{\"customParamMap\":{\"alertMessage\":\"上门新单量(日)合计（不含家修匠及无法服务），点击链接查看详情：![](https://file1.xiujiadian.com/prod/ows/template/file/7219943177772891951.png)\"},\"groupWebhooks\":[{\"accessToken\":\"fa1b193435393c70bf300fcf018e6347ec4af7fa9e8734f72b231dd0a5b7bc7f\",\"name\":\"[内部]-订单策略&运营组\",\"secret\":\"SECcda03ac753f8d8d68f4d4b592e31b30d0d2e5ab26d6cad5d52f65a45cdf2053f\"}]},\"ruleCode\":\"10756\",\"ruleId\":10756,\"serialNumber\":\"AC11030C25670AB7A938673655270117\"}";
}
