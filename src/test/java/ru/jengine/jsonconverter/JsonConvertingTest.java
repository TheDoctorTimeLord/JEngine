package ru.jengine.jsonconverter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.jengine.beancontainer.Constants.Contexts;
import ru.jengine.beancontainer.JEngineContainer;
import ru.jengine.beancontainer.annotations.ContainerModule;
import ru.jengine.beancontainer.annotations.Import;
import ru.jengine.beancontainer.configuration.ContainerConfiguration;
import ru.jengine.beancontainer.modules.AnnotationModule;
import ru.jengine.jsonconverter.additional.*;
import ru.jengine.jsonconverter.modules.EnableJsonConverter;
import ru.jengine.jsonconverter.resources.ResourceLoader;
import ru.jengine.jsonconverter.resources.ResourceMetadata;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class JsonConvertingTest {

    private static JsonConverter jsonConverter;
    private static ResourceLoader resourceLoader;

    @BeforeClass
    public static void setUp() {
        JEngineContainer container = new JEngineContainer(ContainerConfiguration.builder(ConvertingModule.class)
                .addBeans(new CustomResourceLoader())
                .build());
        container.initializeContainerByDefault();

        jsonConverter = container.getBean(JsonConverter.class);
        resourceLoader = container.getBean(ResourceLoader.class);
    }

    @Test
    public void testApplyParentAttributes() {
        resourceLoader.addResourceToCache(
                new ResourceMetadata("ns", null, "path"), "{\"a\": 2, \"c\": 3}"
        );

        A a = jsonConverter.convertFromJson(
                "{\"parent\": \"ns:path\", \"a\": 5}",
                A.class
        );
        assertThat(a.a, is(5));
        assertThat(a.b, nullValue());
        assertThat(a.c, is(3));
    }

    @Test
    public void testFindLinkedAttributes() {
        resourceLoader.addResourceToCache(
                new ResourceMetadata("ns", null, "path"), "{\"a\": 2, \"c\": 3}"
        );

        A a = jsonConverter.convertFromJson(
                "{\"a\": 5, \"b\": \"ns:path\"}",
                A.class
        );
        assertThat(a.a, is(5));
        assertThat(a.b.a, is(2));
        assertThat(a.b.c, is(3));
    }

    @Test
    public void testCreateByInterface() {
        resourceLoader.addResourceToCache(
                new ResourceMetadata("ns", null, "path"), "{\"a\": 2, \"c\": 3}"
        );

        C c = jsonConverter.convertFromJson(
                "{\"a\": 5}",
                C.class
        );
        assertThat(c, instanceOf(D.class));
        assertThat(((D)c).a, is(5));
    }

    @Test
    public void testDeserializeObject() {
        A a = new A();
        a.a = 1;
        a.c = 2;
        a.b = new B();
        a.b.a = 3;
        a.b.c = 4;

        JsonElement result = jsonConverter.convertToJson(a);

        assertThat(result, instanceOf(JsonObject.class));

        JsonObject castedResult = (JsonObject)result;
        assertThat(castedResult.get("a").getAsInt(), is(1));
        assertThat(castedResult.get("c").getAsInt(), is(2));
        assertThat(castedResult.get("b"), instanceOf(JsonObject.class));

        JsonObject innerB = castedResult.getAsJsonObject("b");
        assertThat(innerB.get("a").getAsInt(), is(3));
        assertThat(innerB.get("c").getAsInt(), is(4));
    }

    @ContainerModule(contextName = Contexts.JSON_CONVERTER_CONTEXT)
    @Import({ CustomFormatter.class, ParentResolverFormatter.class, LinkExtractorImpl.class, CDeserializer.class})
    @EnableJsonConverter
    public static class ConvertingModule extends AnnotationModule {}
}
