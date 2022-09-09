import org.example.jpms.api.plugin.Plugin;
import org.example.plugin.PluginMain;

@Plugin(main = PluginMain.class, authors = {"orblazer"})
module org.example.plugin {
    requires transitive org.example.jpms.api;
}