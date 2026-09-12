# 🛸 Rick and Morty - Jetpack Compose UI

## 📝 Descripción del Proyecto
Esta aplicación es una implementación nativa en Android de las vistas principales para un directorio de personajes de Rick and Morty. El proyecto se centra en el diseño visual y la construcción de interfaces de usuario modernas y declarativas.

## 📱 Vistas y Características
* **Splash Screen:** Pantalla de carga inicial con un diseño inmersivo, efecto de portal construido con figuras nativas (`Canvas`) y navegación automática gestionada con corrutinas.
* **Home (Directorio):** Interfaz principal que implementa una lista optimizada (`LazyColumn`) para renderizar los personajes. Incluye una barra de navegación inferior personalizada, tipografía estilizada y tarjetas (Cards) con indicadores de estado (Vivo/Muerto).
* **Detail Screen (Perfil):** Vista expandida del personaje seleccionado. Destaca por el uso de recortes de imagen asimétricos, resplandores con gradientes radiales y un panel estructurado de estadísticas (Especie, Origen, Género).

## 🛠️ Stack Tecnológico
* **Lenguaje:** Kotlin
* **UI Framework:** Jetpack Compose
* **Navegación:** Navigation Compose (`NavHost`, `NavController`)
* **Carga de Imágenes:** Coil (Carga asíncrona mediante URLs)
* **IDE:** Android Studio

## 🖼️ Previsualización de la Interfaz

| Splash Screen | Home | Detail Screen |
| :---: | :---: | :---: |
| <img width="250" src="https://github.com/user-attachments/assets/ac1e95c7-3479-44af-a980-ddfdc56b0906" alt="Splash" /> | <img width="250" src="https://github.com/user-attachments/assets/50d8d3ef-68ab-4871-9fa2-8aacfa149068" alt="Home" /> | <img width="250" src="https://github.com/user-attachments/assets/6c5e5663-90be-462b-abf2-60e34a5eda15" alt="Detail" /> |

## 🚀 Instalación y Uso
1. Clona este repositorio en tu máquina local:
   ```bash
   git clone [https://github.com/SamuPV05/Rick-Morty.git](https://github.com/SamuPV05/Rick-Morty.git)
