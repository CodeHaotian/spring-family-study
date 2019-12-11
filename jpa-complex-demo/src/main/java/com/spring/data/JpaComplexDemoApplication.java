package com.spring.data;

import com.spring.data.model.Coffee;
import com.spring.data.model.CoffeeOrder;
import com.spring.data.model.OrderState;
import com.spring.data.repository.CoffeeOrderRepository;
import com.spring.data.repository.CoffeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Haotian
 * @Date: 2019/12/10 20:25
 * @Description: 启动类
 **/
@SpringBootApplication
@EnableJpaRepositories
@EnableTransactionManagement
@Slf4j
public class JpaComplexDemoApplication implements ApplicationRunner {
    @Autowired
    private CoffeeRepository coffeeRepository;
    @Autowired
    private CoffeeOrderRepository orderRepository;

    public static void main(String[] args) {
        SpringApplication.run( JpaComplexDemoApplication.class, args );
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //ApplicationRunner接口与CommandLineRunner做的事情是一样的，执行时机都是在容器启动完成的时候进行执行。唯一不同的是ApplicationRunner会封装命令行参数，可以获得更详细的命令行参数。
        initOrder();
        findOrders();
    }

    private void initOrder() {
        //添加一杯名为拉铁的咖啡，设置其金额为30.0 CNY
        Coffee latte = Coffee.builder().name( "latte" )
                //指定货币代码为CNY，使用joda-money将double类型的数字转换为货币
                .price( Money.of( CurrencyUnit.of( "CNY" ), 30.0 ) )
                .build();
        //保存数据
        coffeeRepository.save( latte );
        //日志打印咖啡信息
        log.info( "Coffee：{}", latte );

        Coffee espresso = Coffee.builder().name( "espresso" )
                .price( Money.of( CurrencyUnit.of( "CNY" ), 20.0 ) )
                .build();
        coffeeRepository.save( espresso );
        log.info( "Coffee：{}", espresso );

        CoffeeOrder order = CoffeeOrder.builder()
                .customer( "Li Lei" )
                .items( Collections.singletonList( espresso ) )
                .state( OrderState.INIT )
                .build();
        orderRepository.save( order );
        log.info( "Order：{}", order );

        order = CoffeeOrder.builder()
                .customer( "Li Lei" )
                .items( Arrays.asList( espresso, latte ) )
                .state( OrderState.INIT )
                .build();
        orderRepository.save( order );
        log.info( "Order：{}", order );
    }

    private void findOrders() {
        coffeeRepository
                .findAll( Sort.by( Sort.Direction.DESC, "id" ) )
                .forEach( c -> log.info( "Loading {}", c ) );

        List<CoffeeOrder> list = orderRepository.findTop3ByOrderByUpdateTimeDescIdAsc();
        log.info( "findTop3ByOrderByUpdateTimeDescIdAsc：{}", getJoinedOrderId( list ) );

        // 不开启事务会因为没Session而报LazyInitializationException
        list.forEach( o -> {
            log.info( "Order {}", o.getId() );
            o.getItems().forEach( i -> log.info( "Item {}", i ) );
        } );

        list = orderRepository.findByItems_Name( "latte" );
        log.info( "findByItems_Name：{}", getJoinedOrderId( list ) );
    }

    private String getJoinedOrderId(List<CoffeeOrder> list) {
        return list.stream().map( o -> o.getId().toString() )
                .collect( Collectors.joining( "," ) );
    }
}
