package src;

import src.Controlador.MainController;
import src.Modelo.SteamApiService;
import src.Vista.MainView;

public class Main {
    public static void main(String[] args) {
        SteamApiService service = new SteamApiService("06166564FA99EDCBCEDAFFF71732218B");
        MainView view = new MainView();
        MainController controller = new MainController(service, view);
        view.show();
    }
}
