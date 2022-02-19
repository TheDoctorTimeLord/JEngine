package ru.jengine.battlemodule.core.information;

/**
 * Описывает сервис, предоставляющий динамическому объекту доступ к информации (см. {@link InformationCenter}).
 * Важно помнить, что данные сервисы потенциально могут быть использованы в многопоточной среде<br>
 * <b>РЕКОМЕНДАЦИЯ:</b> для своих {@link InformationService}'ов добавляйте интерфейс, расширяющий текущий, который
 * будет декларировать поведение вашего сервиса (service1). Если ваш {@link InformationService} может как-то изменять
 * внутреннюю информацию о текущем состоянии боя, то рекомендуется для этого добавить ещё один интерфейс (service2),
 * расширяющий service1. В service2 добавьте методы, позволяющие изменить данные в вашем сервисе. При
 * {@link ru.jengine.battlemodule.core.contentregistrars.ContentRegistrar регистрации} своего
 * {@link InformationService}, в качестве интерфейса, по которому будет получаться ваш сервис, укажите service1
 */
public interface InformationService {
}
