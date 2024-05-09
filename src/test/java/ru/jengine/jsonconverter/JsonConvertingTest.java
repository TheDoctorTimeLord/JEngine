package ru.jengine.jsonconverter;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.jengine.beancontainer.Constants.Contexts;
import ru.jengine.beancontainer.JEngineContainer;
import ru.jengine.beancontainer.annotations.ContainerModule;
import ru.jengine.beancontainer.annotations.Import;
import ru.jengine.beancontainer.configuration.ContainerConfiguration;
import ru.jengine.beancontainer.modules.AnnotationModule;
import ru.jengine.jsonconverter.additional.*;
import ru.jengine.jsonconverter.modules.EnableJsonConverterWithStandardTools;
import ru.jengine.jsonconverter.resources.ResourceLoader;
import ru.jengine.jsonconverter.resources.ResourceMetadata;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class JsonConvertingTest {
    private static final ResourceMetadata PATH_TOKEN = new ResourceMetadata("ns", null, "path");
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
        resourceLoader.addResourceToCache(PATH_TOKEN, "{\"a\": 2, \"c\": 3}");

        A actual = jsonConverter.convertFromJson(
                "{\"parent\": \"ns:path\", \"a\": 5}",
                A.class
        );
        assertThat(actual.a, is(5));
        assertThat(actual.b, nullValue());
        assertThat(actual.c, is(3));
    }

    @Test
    public void testFindLinkedAttributes() {
        resourceLoader.addResourceToCache(PATH_TOKEN, "{\"a\": 2, \"c\": 3}");

        A actual = jsonConverter.convertFromJson(
                "{\"a\": 5, \"b\": \"ns:path\"}",
                A.class
        );
        assertThat(actual.a, is(5));
        assertThat(actual.b.a, is(2));
        assertThat(actual.b.c, is(3));
    }

    @Test
    public void testCreateByInterface() {
        resourceLoader.addResourceToCache(PATH_TOKEN, "{\"a\": 2, \"c\": 3}");

        C actual = jsonConverter.convertFromJson(
                "{\"a\": 5}",
                C.class
        );
        assertThat(actual, instanceOf(D.class));
        assertThat(((D)actual).a, is(5));
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

    @Test
    public void testHierarchyFormatting() {
        resourceLoader.addResourceToCache(PATH_TOKEN, "true");

        E actual = jsonConverter.convertFromJson(
                """
                     {
                         "check": "ns:path",
                         "child1": {
                             "check": "ns:path",
                             "child1": {
                                 "check": "ns:path"
                             }
                         },
                         "child2": {
                             "check": "ns:path"
                         }
                     }""",
                E.class
        );

        assertThat(actual.getCheck(), is(true));
        assertThat(actual.getChild1(), notNullValue());
        assertThat(actual.getChild2(), notNullValue());

        assertThat(actual.getChild1().getCheck(), is(true));
        assertThat(actual.getChild1().getChild1(), notNullValue());
        assertThat(actual.getChild1().getChild2(), nullValue());

        assertThat(actual.getChild1().getChild1().getCheck(), is(true));
        assertThat(actual.getChild1().getChild1().getChild1(), nullValue());
        assertThat(actual.getChild1().getChild1().getChild2(), nullValue());

        assertThat(actual.getChild2().getCheck(), is(true));
        assertThat(actual.getChild2().getChild1(), nullValue());
        assertThat(actual.getChild2().getChild2(), nullValue());
    }

    @Test
    public void testConvertingTransient() {
        F actual = jsonConverter.convertFromJson("""
                {"notTransientField": 5, "transientField": 5}""",
                F.class);

        assertThat(actual.getTransientField(), is(0));
        assertThat(actual.getNotTransientField(), is(5));
    }

    @Test
    public void testOverridingFields() {
        resourceLoader.addResourceToCache(PATH_TOKEN, """
                {
                    "field1": [1, 2],
                    "field2": [1, 2],
                    "field3": [1, 2, 3, 4],
                    "field4": {"1": 1},
                    "field5": {"1": 3},
                    "field6": {"1": 3, "2": 3, "3": 3},
                    "field7": [1, 2, 3, 4, 5, 6],
                    "field8": {"1": 3, "2": 3, "3": 3, "4": 5, "5": 5}
                }""");

        JsonObject actual = jsonConverter.convertFromJson("""
                {
                    "parent": "ns:path",
                    "field1": [1, 2, 3, 4],
                    "+field2": [3, 4],
                    "field4": {"1": 3, "2": 3, "3": 3},
                    "+field5": {"2": 3, "3": 3},
                    "-field7": [5, 6],
                    "-field8": {"4": 5, "5": 5}
                }""",
                JsonObject.class);

        JsonArray expectedJsonArray = new JsonArray();
        expectedJsonArray.add(1);
        expectedJsonArray.add(2);
        expectedJsonArray.add(3);
        expectedJsonArray.add(4);

        JsonObject expectedJsonObject = new JsonObject();
        expectedJsonObject.addProperty("1", 3);
        expectedJsonObject.addProperty("2", 3);
        expectedJsonObject.addProperty("3", 3);

        Assert.assertEquals(expectedJsonArray, actual.get("field1"));
        Assert.assertEquals(expectedJsonArray, actual.get("field2"));
        Assert.assertEquals(expectedJsonArray, actual.get("field3"));
        Assert.assertEquals(expectedJsonObject, actual.get("field4"));
        Assert.assertEquals(expectedJsonObject, actual.get("field5"));
        Assert.assertEquals(expectedJsonObject, actual.get("field6"));
        Assert.assertEquals(expectedJsonArray, actual.get("field7"));
        Assert.assertEquals(expectedJsonObject, actual.get("field8"));
    }

    @ContainerModule(contextName = Contexts.JSON_CONVERTER_CONTEXT)
    @Import({ CustomFormatter.class, ParentResolverFormatter.class, LinkExtractorImpl.class, CDeserializer.class})
    @EnableJsonConverterWithStandardTools
    public static class ConvertingModule extends AnnotationModule {}
}
