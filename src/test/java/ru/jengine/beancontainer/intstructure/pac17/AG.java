package ru.jengine.beancontainer.intstructure.pac17;

import ru.jengine.beancontainer.annotations.Api;
import ru.jengine.beancontainer.annotations.Api.ApiElement;

public class AG {
    @Api(@ApiElement(index = 0, message = EmptyParameter.API_MESSAGE))
    public AG(EmptyParameter parameter) { }
}
