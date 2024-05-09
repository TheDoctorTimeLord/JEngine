package ru.jengine.infrastructure.beancontainer.pac9;

import ru.jengine.beancontainer.annotations.Bean;
import ru.jengine.beancontainer.annotations.Order;

@Bean
@Order(-10)
public class Q implements Ordered {
}
