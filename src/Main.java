package src;

import src.Controlador.DashboardController;
import src.Modelo.SteamApiService;
import src.Vista.DashboardView;

public class Main {
    public static void main(String[] args) {
        SteamApiService service = new SteamApiService("06166564FA99EDCBCEDAFFF71732218B");
        DashboardView view = new DashboardView();
        new DashboardController(service, view);
        view.show();
    }
}
