package com.enation.eop.sdk.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

//@Configuration
@EnableSwagger2
@ComponentScan(basePackages = { "com.enation" })
public class Swagger2 {

	@Bean
	public Docket createCoreApi() {
		return new Docket(DocumentationType.SWAGGER_2)

				.groupName("core分组").apiInfo(apiInfo()).select()

				.paths(PathSelectors.ant("/core/**")).build();
	}

	@Bean
	public Docket createApiCoreApi() {
		return new Docket(DocumentationType.SWAGGER_2)

				.groupName("ApiCore分组").apiInfo(apiInfo()).select()

				.paths(PathSelectors.ant("/api/core/**")).build();
	}

	@Bean
	public Docket createShopApi() {
		return new Docket(DocumentationType.SWAGGER_2)

				.groupName("Shop分组").apiInfo(apiInfo()).select()

				.paths(PathSelectors.ant("/shop/**")).build();
	}

	@Bean
	public Docket createApiShopApi() {
		return new Docket(DocumentationType.SWAGGER_2)

				.groupName("ApiShop分组").apiInfo(apiInfo()).select()

				.paths(PathSelectors.ant("/api/shop/**")).build();
	}

	@Bean
	public Docket createCmsApi() {
		return new Docket(DocumentationType.SWAGGER_2)

				.groupName("Cms分组").apiInfo(apiInfo()).select()

				.paths(PathSelectors.ant("/cms/**")).build();
	}

	@Bean
	public Docket createApiCmsApi() {
		return new Docket(DocumentationType.SWAGGER_2)

				.groupName("ApiCms分组").apiInfo(apiInfo()).select()

				.paths(PathSelectors.ant("/api/cms/**")).build();
	}

	@Bean
	public Docket createAfterSaleApi() {
		return new Docket(DocumentationType.SWAGGER_2)

				.groupName("售后分组").apiInfo(apiInfo()).select()

				.paths(PathSelectors.ant("/after-sale/**")).build();
	}

	@Bean
	public Docket createGoodsInfoApi() {
		return new Docket(DocumentationType.SWAGGER_2)

				.groupName("商品分组").apiInfo(apiInfo()).select()

				.paths(PathSelectors.ant("/goods-info/**")).build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Javashop RESTful APIs Document")
				.description("更多javashop相关信息请关注：http://www.javamall.com.cn/")
				.termsOfServiceUrl("http://www.javamall.com.cn/").contact("易族智汇（北京）科技有限公司").version("6.4").build();
	}

}
