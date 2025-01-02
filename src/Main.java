package src;

import src.Controlador.StartController;
import src.Modelo.SteamApiService;
import src.Vista.StartView;

public class Main {
    public static void main(String[] args) {
        SteamApiService service = SteamApiService.getInstance("06166564FA99EDCBCEDAFFF71732218B");
        StartView startView = new StartView();
        new StartController(service, startView);
        startView.show();
    }
}
