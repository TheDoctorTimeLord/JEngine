package ru.jengine.infrastructure.beancontainer.pac8;

import ru.jengine.beancontainer.Constants;
import ru.jengine.beancontainer.annotations.Bean;

@Bean(scopeName = Constants.BeanScope.PROTOTYPE)
public class M implements Int {
}
