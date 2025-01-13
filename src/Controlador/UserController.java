package src.Controlador;

import src.Modelo.Data.UserModel;
import src.Vista.MainViews.DashboardView;
import src.Vista.ViewManager;

public class UserController {
    private final DashboardView dashboardView;
    private final UserModel userModel;
    private final ViewManager viewManager;

    public UserController(DashboardView dashboardView, UserModel userModel, ViewManager viewManager) {
        this.dashboardView = dashboardView;
        this.userModel = userModel;
        this.viewManager = viewManager;
    }

    public void displayUserInfo() {
        try {
            viewManager.displayUserInfo(userModel);
        } catch (Exception ex) {
            dashboardView.avatarLabel.setText("Avatar no disponible");
            System.err.println("Error al cargar la información del usuario: " + ex.getMessage());
        }
    }
}