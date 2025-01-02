package src;

import src.Controlador.StartController;
import src.Modelo.SteamApiService;
import src.Vista.StartView;

public class Main {
    public static void main(String[] args) {
        SteamApiService service = new SteamApiService("06166564FA99EDCBCEDAFFF71732218B");
        StartView view = new StartView();
        new StartController(service, view);
        view.show();
    }
}
