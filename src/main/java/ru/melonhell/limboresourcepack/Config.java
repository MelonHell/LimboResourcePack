package ru.melonhell.limboresourcepack;

import net.elytrium.commons.config.YamlConfig;
import net.elytrium.commons.kyori.serialization.Serializers;


public class Config extends YamlConfig {
    @Comment({
            "Available serializers:",
            "LEGACY_AMPERSAND - \"&c&lExample &c&9Text\".",
            "LEGACY_SECTION - \"§c§lExample §c§9Text\".",
            "MINIMESSAGE - \"<bold><red>Example</red> <blue>Text</blue></bold>\". (https://webui.adventure.kyori.net/)",
            "GSON - \"[{\"text\":\"Example\",\"bold\":true,\"color\":\"red\"},{\"text\":\" \",\"bold\":true},{\"text\":\"Text\",\"bold\":true,\"color\":\"blue\"}]\". (https://minecraft.tools/en/json_text.php/)",
            "GSON_COLOR_DOWNSAMPLING - Same as GSON, but uses downsampling."
    })
    public Serializers SERIALIZER = Serializers.MINIMESSAGE;

    @Comment("Урл для респака")
    public String RP_URL = "http://localhost/resourcepack.zip";

    @Comment("Блядский текст при запросе на респак")
    public String RP_PROMPT_MESSAGE = "сук скачай респак";

    @Comment("Если ошибка то кикает с этим текстом")
    public String RP_FAILURE_KICK_MESSAGE = "Произошла ошибка при загрузке пакета ресурсов.<br>Попробуй ещё раз.";

    @Comment("Если юзверь отклонил рп то кикает с этим текстом")
    public String RP_DECLINED_KICK_MESSAGE = "Прими рп<br>(если не отклонял ничё то нажми на серв, потом редактировать и пакеты ресурсов разреши)";

    @Comment("Не пускать чела на серв без респака (Клиентская фича, по идее можно обойти читом)")
    public boolean RP_FORCE = true;

    @Comment("Кикнуть если отклонил")
    public boolean KICK_IF_DECLINED = true;

    @Comment("Кикнуть при ошибке")
    public boolean KICK_IF_FAILURE = true;
}
