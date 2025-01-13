# DapPlay: Aplicación de Gestión y Visualización de Videojuegos

## Descripción

**DapPlay** es una aplicación interactiva que permite a los usuarios gestionar, visualizar y explorar información relacionada con videojuegos. Implementada usando el patrón **Modelo-Vista-Controlador (MVC)**, la aplicación separa las responsabilidades en tres capas principales: **Modelo**, **Vista** y **Controlador**, lo que facilita la escalabilidad y el mantenimiento del código.

El proyecto está diseñado para integrarse con diversas APIs, como **Steam** para la gestión de juegos y logros, y **YouTube** para la visualización de videos relacionados con los videojuegos.

## Funcionalidades

- **Conexión con la API de Steam**: Permite acceder a los juegos y logros del usuario.
- **Gestión de Favoritos**: Los usuarios pueden agregar y gestionar sus juegos favoritos.
- **Visualización de Gráficos**: Se pueden visualizar gráficos de los logros desbloqueados y la actividad de los juegos.
- **Videos Relacionados**: Integración con YouTube para mostrar videos relacionados con los juegos.
- **Interacción Social**: Visualización de amigos y logros.

## Estructura del Proyecto

### 1. **Modelo**

- **SteamApiService**: Interactúa con la API de Steam.
- **YoutubeApiService**: Interactúa con la API de YouTube.
- **FavoritesManager**: Gestiona los juegos favoritos.
- **FavoritesObserver**: Interfaz para implementar el observador.
- **GameplayModel**: Modelo que representa los datos de gameplay.
- **UserModel**: Modelo que contiene los datos del usuario.
- **SortStrategy**: Define las estrategias para ordenar los juegos.
- **SortByName**: Estrategia para ordenar juegos por nombre.
- **SortByPlaytime**: Estrategia para ordenar juegos por tiempo de juego.
- **ImageUtil**: Utilidad para manejar imágenes dentro de la aplicación.

### 2. **Vista**

- **ChartStrategy**: Interfaz para la creación de gráficos.
- **BarChartStrategy**: Estrategia para mostrar gráficos de barras.
- **PieChartStrategy**: Estrategia para mostrar gráficos de pastel.
- **LineChartStrategy**: Estrategia para mostrar gráficos de líneas.
- **ScatterChartStrategy**: Estrategia para mostrar gráficos de dispersión.
- **BarAchievementsChart**: Estrategia específica para los gráficos de logros desbloqueados.
- **ChartDecorator**: Clase decoradora para personalizar los gráficos.
- **ColorDecorator**: Decorador para agregar color a los gráficos.
- **TitleDecorator**: Decorador para agregar títulos a los gráficos.
- **DashboardView**: Vista principal que muestra las estadísticas y los gráficos.
- **StartView**: Vista de inicio para la aplicación.
- **AchievementPanelFactory**: Factory para crear los paneles de logros.
- **ChartPanelFactory**: Factory para crear paneles con gráficos.
- **FavoritePanelFactory**: Factory para crear paneles con juegos favoritos.
- **FriendPanelFactory**: Factory para crear paneles con amigos.
- **GamePanelFactory**: Factory para crear paneles con juegos.
- **GameplayPanel**: Panel que muestra los detalles del gameplay.
- **PanelFactory**: Factory general para crear diferentes tipos de paneles.
- **UserPanelFactory**: Factory para crear paneles con información de usuarios.
- **ViewManager**: Gestiona la vista general de la aplicación.

### 3. **Controlador**

- **AchievementsController**: Controla la carga y visualización de logros.
- **ChartController**: Controla la generación de gráficos en la vista.
- **DashboardController**: Controla la vista principal y maneja la interacción con la API.
- **StartController**: Controla la vista de inicio de la aplicación.
- **UserController**: Controla la información del usuario y su visualización.
- **GameplayController**: Controla la visualización de los gameplays relacionados con los juegos.

## Patrones de Diseño Implementados

- **Singleton**: Usado en **SteamApiService** para asegurar que solo exista una instancia de la clase.
- **Observer**: Usado en **FavoritesManager** para notificar a los observadores sobre cambios en los favoritos.
- **Factory Method**: Usado para crear los paneles de gráficos en **PanelFactory**.
- **Strategy**: Usado para cambiar dinámicamente la estrategia de ordenación en **SortStrategy**.
- **Decorator**: Usado para agregar funcionalidades adicionales a los gráficos, como colores o títulos.

## Uso

### Pantalla de Inicio:
- Al iniciar la aplicación, los usuarios deben ingresar su nombre de usuario de Steam para acceder a la vista principal.

### Vista Principal:
- En la vista principal, los usuarios pueden ver sus juegos favoritos, logros, amigos y estadísticas.
- La interfaz incluye gráficos de barras, pastel y líneas que muestran la actividad del usuario en Steam.

### Interacción Social:
- Los usuarios pueden ver a sus amigos y sus logros directamente desde la vista principal.

## Instalación

### Pasos de Instalación

1. **Clona el repositorio**:

   ```bash
   git clone https://github.com/alu0101203942/DapPlay.git
