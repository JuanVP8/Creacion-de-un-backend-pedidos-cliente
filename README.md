# Creacion-de-un-backend-pedidos-cliente
He diseñado y creado un backend basado en Rest para una app que gestiona pedidos de clientes. también he diseñado su flujo en un archivo bpel con su composite app usando herramientas como soapui o postman.

### Pruebas WS-BPEL
 - URL a los documentos WSDL que es necesario utilizar:
 OrderService → http://localhost:7070/OrderService?wsdl  
 InventoryService → http://localhost:7070/InventoryService?wsdl  
 BillingService → http://localhost:7070/BillingService?wsdl  
 ShippingService → http://localhost:7070/ShippingService?wsdl  
 OrderProcess (flujo BPEL) → http://localhost:9080/OrderProcess?wsdl

- El sistema sigue una **arquitectura en capas**:
- **Modelo:** Clases `Customer`, `Order`, `OrderLine`, `OrderStatus`.
- **Servicio:** Interfaz `OrderService` y su implementación `MockOrderService`.
- **Orquestación:** Procesos **WS-BPEL** desplegados en OpenESB y organizados en una **Composite Application (CA)**.

- Se mantuvo una **convención de commits** estandarizada para asegurar trazabilidad:
- `[ADD-CUS]`, `[UPD-CUS]`, `[RM-CUS]`, `[FDN-CUS]`, `[ADD-ORD]`, `[UPD-ORD]`, `[FDN-ORD]`, `[EXCEP]`, `[BPEL]`, `[UTIL]`, `[TST]`.
- Esto permite identificar de forma directa qué miembro implementó cada caso de uso.

- Se implementaron **tests unitarios con JUnit 5**, verificando el correcto funcionamiento de las operaciones y las validaciones de estado.  
Además, se añadió un método `tearDown()` para reiniciar el servicio tras cada prueba y asegurar independencia entre tests.

---

# Running the project example
---------------------------------------------------------------------

## Running the orders service with Maven/Jetty.

    cd rs-orders/rs-orders-service
    mvn jetty:run


## Running the orders client application

- Configure `rs-orders/rs-orders-client/src/main/resources/ConfigurationParameters.properties`
  for specifying the client project service implementation (XML or JSON) and the port number 
  of the web server in the endpoint address (7070 for Jetty)
  
- Change to `rs-orders-client` folder

    cd rs-orders/rs-orders-client


- AddCustomer

    mvn exec:java -Dexec.mainClass="es.udc.rs.orders.client.ui.OrderServiceClient" -Dexec.args="-addCustomer 'New Customer'"

- FindCustomer

  mvn exec:java -Dexec.mainClass="es.udc.rs.orders.client.ui.OrderServiceClient" -Dexec.args="-findCustomer 1"



PARTE REST / JAX-RS / CLIENTE DE LÍNEA DE COMANDOS
============================================================

Requisitos previos:
- Tener instalado Maven y JDK.
- Tener arrancado el servicio REST rs-orders-service.
- Los servicios mock de la capa modelo mantienen estado en memoria:
  si se para y arranca Jetty, se reinician los identificadores y los datos.

------------------------------------------------------------
0. Compilar todo el proyecto
------------------------------------------------------------

Desde el directorio raíz del proyecto Maven:

cd ia-15-1
mvn clean install


------------------------------------------------------------
1. Arrancar el servicio REST con Jetty
------------------------------------------------------------

En una terminal, desde el módulo rs-orders-service:

cd rs-orders-service
mvn jetty:run

Comprobar que el servicio está arrancado:

http://localhost:7070/rs-orders-service/customers

Si no hay clientes, la respuesta esperada es una lista vacía.

OpenAPI, si está implementado:

http://localhost:7070/rs-orders-service/openapi
http://localhost:7070/rs-orders-service/openapi.json
http://localhost:7070/rs-orders-service/openapi.yaml


------------------------------------------------------------
2. Pruebas automatizadas REST opcionales
------------------------------------------------------------

Desde el directorio raíz del proyecto:

mvn -pl rs-orders-service test


============================================================
BLOQUE A - CLIENTE DE LÍNEA DE COMANDOS USANDO XML
============================================================

------------------------------------------------------------
A.1 Configurar el cliente para XML
------------------------------------------------------------

Editar el fichero:

rs-orders-client/src/main/resources/ConfigurationParameters.properties

Debe quedar así:

ClientOrderServiceFactory.className=es.udc.rs.orders.client.service.rest.RestClientOrderServiceXml
#ClientOrderServiceFactory.className=es.udc.rs.orders.client.service.rest.RestClientOrderServiceJson

RestClientOrderService.endpointAddress=http://localhost:7070/rs-orders-service/

Recompilar el cliente:

mvn -pl rs-orders-client clean compile


------------------------------------------------------------
A.2 Añadir dos clientes con el cliente de línea de comandos
------------------------------------------------------------

Desde el directorio raíz del proyecto:

mvn -pl rs-orders-client exec:java "-Dexec.mainClass=es.udc.rs.orders.client.ui.OrderServiceClient" "-Dexec.args=-addCustomer ClientePrimeroXML 12345678J ElvinaSN client1xml@site.es"

Resultado esperado:
- Customer 1 created successfully
- Se muestra el nombre, DNI, dirección y email del cliente creado.

mvn -pl rs-orders-client exec:java "-Dexec.mainClass=es.udc.rs.orders.client.ui.OrderServiceClient" "-Dexec.args=-addCustomer ClienteSegundoXML 87654321H MariaPitaSN client2xml@site.es"

Resultado esperado:
- Customer 2 created successfully
- Se muestra el nombre, DNI, dirección y email del cliente creado.


------------------------------------------------------------
A.3 Crear pedidos por Postman/SoapUI en XML
------------------------------------------------------------

El cliente de línea de comandos no crea pedidos, por lo que se crean con Postman/SoapUI.

Petición Order 1:

Método:
POST

URL:
http://localhost:7070/rs-orders-service/orders

Headers:
Content-Type: application/xml
Accept: application/xml

Body:

<order>
   <customerId>1</customerId>
   <orderLine>
      <productId>7</productId>
      <quantity>1</quantity>
      <price>19.95</price>
   </orderLine>
</order>

Resultado esperado:
- HTTP 201 Created.
- Se devuelve el pedido creado.
- orderId = 1.
- orderStatus = PENDING.


Petición Order 2:

Método:
POST

URL:
http://localhost:7070/rs-orders-service/orders

Headers:
Content-Type: application/xml
Accept: application/xml

Body:

<order>
   <customerId>1</customerId>
   <orderLine>
      <productId>1</productId>
      <quantity>2</quantity>
      <price>10.55</price>
   </orderLine>
   <orderLine>
      <productId>3</productId>
      <quantity>1</quantity>
      <price>11.45</price>
   </orderLine>
   <orderLine>
      <productId>5</productId>
      <quantity>3</quantity>
      <price>4.95</price>
   </orderLine>
</order>

Resultado esperado:
- HTTP 201 Created.
- Se devuelve el pedido creado.
- orderId = 2.
- orderStatus = PENDING.


------------------------------------------------------------
A.4 Cambiar estado de pedidos con el cliente XML
------------------------------------------------------------

Intentar transición inválida PENDING -> PROCESSED:

mvn -pl rs-orders-client exec:java "-Dexec.mainClass=es.udc.rs.orders.client.ui.OrderServiceClient" "-Dexec.args=-changeOrderStatus 1 PROCESSED"

Resultado esperado:
- Error HTTP 409 Conflict o excepción equivalente.
- La transición PENDING -> PROCESSED no está permitida.


Transición válida PENDING -> PROCESSING:

mvn -pl rs-orders-client exec:java "-Dexec.mainClass=es.udc.rs.orders.client.ui.OrderServiceClient" "-Dexec.args=-changeOrderStatus 1 PROCESSING"

Resultado esperado:
- Order status changed successfully.
- Status: PROCESSING.


Transición válida PROCESSING -> PROCESSED:

mvn -pl rs-orders-client exec:java "-Dexec.mainClass=es.udc.rs.orders.client.ui.OrderServiceClient" "-Dexec.args=-changeOrderStatus 1 PROCESSED"

Resultado esperado:
- Order status changed successfully.
- Status: PROCESSED.


Intentar transición inválida PROCESSED -> PROCESSING:

mvn -pl rs-orders-client exec:java "-Dexec.mainClass=es.udc.rs.orders.client.ui.OrderServiceClient" "-Dexec.args=-changeOrderStatus 1 PROCESSING"

Resultado esperado:
- Error HTTP 409 Conflict o excepción equivalente.
- La transición PROCESSED -> PROCESSING no está permitida.


------------------------------------------------------------
A.5 Buscar pedidos de un cliente con el cliente XML
------------------------------------------------------------

Buscar todos los pedidos del cliente 1, paginación from=0, max=2:

mvn -pl rs-orders-client exec:java "-Dexec.mainClass=es.udc.rs.orders.client.ui.OrderServiceClient" "-Dexec.args=-findOrdersByCustomer 1 0 2"

Resultado esperado:
- Aparecen Order id: 1 y Order id: 2.
- Se muestra fecha e importe total de cada pedido.


Buscar pedidos PROCESSED del cliente 1:

mvn -pl rs-orders-client exec:java "-Dexec.mainClass=es.udc.rs.orders.client.ui.OrderServiceClient" "-Dexec.args=-findOrdersByCustomer 1 0 10 PROCESSED"

Resultado esperado:
- Aparece Order id: 1.
- No aparece Order id: 2 si continúa en PENDING.


Buscar pedidos PENDING del cliente 1:

mvn -pl rs-orders-client exec:java "-Dexec.mainClass=es.udc.rs.orders.client.ui.OrderServiceClient" "-Dexec.args=-findOrdersByCustomer 1 0 10 PENDING"

Resultado esperado:
- Aparece Order id: 2.
- No aparece Order id: 1 porque ya está PROCESSED.


Buscar pedidos de un cliente inexistente:

mvn -pl rs-orders-client exec:java "-Dexec.mainClass=es.udc.rs.orders.client.ui.OrderServiceClient" "-Dexec.args=-findOrdersByCustomer 9999 0 10"

Resultado esperado:
- Error HTTP 404 Not Found o excepción equivalente.


------------------------------------------------------------
A.6 Eliminar clientes con el cliente XML
------------------------------------------------------------

Intentar eliminar cliente 1, que tiene pedidos:

mvn -pl rs-orders-client exec:java "-Dexec.mainClass=es.udc.rs.orders.client.ui.OrderServiceClient" "-Dexec.args=-removeCustomer 1"

Resultado esperado:
- Error HTTP 409 Conflict o excepción equivalente.
- No se puede eliminar un cliente con pedidos.


Eliminar cliente 2, que no tiene pedidos:

mvn -pl rs-orders-client exec:java "-Dexec.mainClass=es.udc.rs.orders.client.ui.OrderServiceClient" "-Dexec.args=-removeCustomer 2"

Resultado esperado:
- Customer 2 removed successfully.


Eliminar un cliente inexistente:

mvn -pl rs-orders-client exec:java "-Dexec.mainClass=es.udc.rs.orders.client.ui.OrderServiceClient" "-Dexec.args=-removeCustomer 9999"

Resultado esperado:
- Error HTTP 404 Not Found o excepción equivalente.


============================================================
BLOQUE B - CLIENTE DE LÍNEA DE COMANDOS USANDO JSON
============================================================

IMPORTANTE:
Para ejecutar este bloque desde estado inicial, parar y volver a arrancar Jetty en rs-orders-service.
De esta forma el mock se reinicia y los identificadores vuelven a empezar en 1.
Si no se reinicia Jetty, cambiar los DNI y los identificadores en las pruebas según corresponda.

------------------------------------------------------------
B.1 Configurar el cliente para JSON
------------------------------------------------------------

Editar el fichero:

rs-orders-client/src/main/resources/ConfigurationParameters.properties

Debe quedar así:

#ClientOrderServiceFactory.className=es.udc.rs.orders.client.service.rest.RestClientOrderServiceXml
ClientOrderServiceFactory.className=es.udc.rs.orders.client.service.rest.RestClientOrderServiceJson

RestClientOrderService.endpointAddress=http://localhost:7070/rs-orders-service/

Recompilar el cliente:

mvn -pl rs-orders-client clean compile


------------------------------------------------------------
B.2 Añadir dos clientes con el cliente de línea de comandos
------------------------------------------------------------

Desde el directorio raíz del proyecto:

mvn -pl rs-orders-client exec:java "-Dexec.mainClass=es.udc.rs.orders.client.ui.OrderServiceClient" "-Dexec.args=-addCustomer ClientePrimeroJSON 12345678J ElvinaSN client1json@site.es"

Resultado esperado:
- Customer 1 created successfully.

mvn -pl rs-orders-client exec:java "-Dexec.mainClass=es.udc.rs.orders.client.ui.OrderServiceClient" "-Dexec.args=-addCustomer ClienteSegundoJSON 87654321H MariaPitaSN client2json@site.es"

Resultado esperado:
- Customer 2 created successfully.


------------------------------------------------------------
B.3 Crear pedidos por Postman/SoapUI en JSON
------------------------------------------------------------

El cliente de línea de comandos no crea pedidos, por lo que se crean con Postman/SoapUI.

Petición Order 1:

Método:
POST

URL:
http://localhost:7070/rs-orders-service/orders

Headers:
Content-Type: application/json
Accept: application/json

Body:

{
  "customerId": 1,
  "orderLine": [
    {
      "productId": 7,
      "quantity": 1,
      "price": 19.95
    }
  ]
}

Resultado esperado:
- HTTP 201 Created.
- Se devuelve el pedido creado.
- orderId = 1.
- orderStatus = PENDING.


Petición Order 2:

Método:
POST

URL:
http://localhost:7070/rs-orders-service/orders

Headers:
Content-Type: application/json
Accept: application/json

Body:

{
  "customerId": 1,
  "orderLine": [
    {
      "productId": 1,
      "quantity": 2,
      "price": 10.55
    },
    {
      "productId": 3,
      "quantity": 1,
      "price": 11.45
    },
    {
      "productId": 5,
      "quantity": 3,
      "price": 4.95
    }
  ]
}

Resultado esperado:
- HTTP 201 Created.
- Se devuelve el pedido creado.
- orderId = 2.
- orderStatus = PENDING.

Nota JSON:
El campo debe llamarse "orderLine", no "orderLines", porque el DTO expone ese nombre.


------------------------------------------------------------
B.4 Cambiar estado de pedidos con el cliente JSON
------------------------------------------------------------

Intentar transición inválida PENDING -> PROCESSED:

mvn -pl rs-orders-client exec:java "-Dexec.mainClass=es.udc.rs.orders.client.ui.OrderServiceClient" "-Dexec.args=-changeOrderStatus 1 PROCESSED"

Resultado esperado:
- Error HTTP 409 Conflict o excepción equivalente.


Transición válida PENDING -> PROCESSING:

mvn -pl rs-orders-client exec:java "-Dexec.mainClass=es.udc.rs.orders.client.ui.OrderServiceClient" "-Dexec.args=-changeOrderStatus 1 PROCESSING"

Resultado esperado:
- Order status changed successfully.
- Status: PROCESSING.


Transición válida PROCESSING -> PROCESSED:

mvn -pl rs-orders-client exec:java "-Dexec.mainClass=es.udc.rs.orders.client.ui.OrderServiceClient" "-Dexec.args=-changeOrderStatus 1 PROCESSED"

Resultado esperado:
- Order status changed successfully.
- Status: PROCESSED.


Intentar transición inválida PROCESSED -> PROCESSING:

mvn -pl rs-orders-client exec:java "-Dexec.mainClass=es.udc.rs.orders.client.ui.OrderServiceClient" "-Dexec.args=-changeOrderStatus 1 PROCESSING"

Resultado esperado:
- Error HTTP 409 Conflict o excepción equivalente.


------------------------------------------------------------
B.5 Buscar pedidos de un cliente con el cliente JSON
------------------------------------------------------------

Buscar todos los pedidos del cliente 1, paginación from=0, max=2:

mvn -pl rs-orders-client exec:java "-Dexec.mainClass=es.udc.rs.orders.client.ui.OrderServiceClient" "-Dexec.args=-findOrdersByCustomer 1 0 2"

Resultado esperado:
- Aparecen Order id: 1 y Order id: 2.


Buscar pedidos PROCESSED del cliente 1:

mvn -pl rs-orders-client exec:java "-Dexec.mainClass=es.udc.rs.orders.client.ui.OrderServiceClient" "-Dexec.args=-findOrdersByCustomer 1 0 10 PROCESSED"

Resultado esperado:
- Aparece Order id: 1.


Buscar pedidos PENDING del cliente 1:

mvn -pl rs-orders-client exec:java "-Dexec.mainClass=es.udc.rs.orders.client.ui.OrderServiceClient" "-Dexec.args=-findOrdersByCustomer 1 0 10 PENDING"

Resultado esperado:
- Aparece Order id: 2.


Buscar pedidos de un cliente inexistente:

mvn -pl rs-orders-client exec:java "-Dexec.mainClass=es.udc.rs.orders.client.ui.OrderServiceClient" "-Dexec.args=-findOrdersByCustomer 9999 0 10"

Resultado esperado:
- Error HTTP 404 Not Found o excepción equivalente.


------------------------------------------------------------
B.6 Eliminar clientes con el cliente JSON
------------------------------------------------------------

Intentar eliminar cliente 1, que tiene pedidos:

mvn -pl rs-orders-client exec:java "-Dexec.mainClass=es.udc.rs.orders.client.ui.OrderServiceClient" "-Dexec.args=-removeCustomer 1"

Resultado esperado:
- Error HTTP 409 Conflict o excepción equivalente.


Eliminar cliente 2, que no tiene pedidos:

mvn -pl rs-orders-client exec:java "-Dexec.mainClass=es.udc.rs.orders.client.ui.OrderServiceClient" "-Dexec.args=-removeCustomer 2"

Resultado esperado:
- Customer 2 removed successfully.


Eliminar un cliente inexistente:

mvn -pl rs-orders-client exec:java "-Dexec.mainClass=es.udc.rs.orders.client.ui.OrderServiceClient" "-Dexec.args=-removeCustomer 9999"

Resultado esperado:
- Error HTTP 404 Not Found o excepción equivalente.


============================================================
PETICIONES REST COMPLEMENTARIAS PARA POSTMAN/SOAPUI
============================================================

Estas peticiones son necesarias para cubrir operaciones REST que no se ejecutan desde el cliente de línea de comandos.

------------------------------------------------------------
C.1 Actualizar cliente 2 en XML
------------------------------------------------------------

Método:
PUT

URL:
http://localhost:7070/rs-orders-service/customers/2

Headers:
Content-Type: application/xml
Accept: application/xml

Body:

<customer>
   <customerId>2</customerId>
   <name>ClienteSegundoModificadoXML</name>
   <dni>87654321H</dni>
   <address>MariaPitaNumero1</address>
   <email>client2xml@site.es</email>
</customer>

Resultado esperado:
- HTTP 200 OK o 204 No Content, según implementación.


------------------------------------------------------------
C.2 Actualizar cliente 2 en JSON
------------------------------------------------------------

Método:
PUT

URL:
http://localhost:7070/rs-orders-service/customers/2

Headers:
Content-Type: application/json
Accept: application/json

Body:

{
  "customerId": 2,
  "name": "ClienteSegundoModificadoJSON",
  "dni": "87654321H",
  "address": "MariaPitaNumero1",
  "email": "client2json@site.es"
}

Resultado esperado:
- HTTP 200 OK o 204 No Content, según implementación.


------------------------------------------------------------
C.3 Buscar cliente por DNI
------------------------------------------------------------

XML o JSON, cambiando el header Accept:

GET http://localhost:7070/rs-orders-service/customers/dni/87654321H


Resultado esperado:
- HTTP 200 OK.
- Devuelve el cliente correspondiente.


Cliente inexistente:

http://localhost:7070/rs-orders-service/customers/dni/33333333A

Resultado esperado:
- HTTP 404 Not Found.


------------------------------------------------------------
C.4 Buscar cliente por id
------------------------------------------------------------

GET http://localhost:7070/rs-orders-service/customers/1

Resultado esperado:
- HTTP 200 OK.
- Devuelve el cliente 1.

GET http://localhost:7070/rs-orders-service/customers/9999

Resultado esperado:
- HTTP 404 Not Found.


------------------------------------------------------------
C.5 Buscar clientes por texto en el nombre
------------------------------------------------------------

GET http://localhost:7070/rs-orders-service/customers?keywords=Cliente


Resultado esperado:
- HTTP 200 OK.
- Devuelve una lista paginada de clientes.
- Cada elemento incluye únicamente customerId, dni y name.


------------------------------------------------------------
C.6 Obtener información de pedidos y recursos HATEOAS
------------------------------------------------------------

Obtener pedidos de cliente 1:

GET http://localhost:7070/rs-orders-service/orders/customer/1?from=0&max=10

Resultado esperado:
- HTTP 200 OK.
- Devuelve la lista resumida de pedidos del cliente 1.


Obtener información del pedido 1:

GET http://localhost:7070/rs-orders-service/orders/1

Resultado esperado:
- HTTP 200 OK.
- Devuelve el pedido completo.


---

## PRUEBAS OPCIONALES DE HIPERMEDIA / HATEOAS

Requisito previo:

* Tener arrancado el servicio REST:

cd rs-orders-service
mvn jetty:run

* Tener creados al menos:

  * Cliente 1 con dos pedidos.
  * Cliente 2 sin pedidos.

Si no existen, crearlos antes con los comandos anteriores del cliente o con Postman.

---

1. Comprobar enlaces hipermedia de un cliente con pedidos

---

Comando:

curl.exe -i -H "Accept: application/json" http://localhost:7070/rs-orders-service/customers/1

Resultado esperado:

* Código HTTP 200 OK.
* La respuesta del cliente debe incluir enlaces hipermedia.
* Debe aparecer un enlace self al propio cliente.
* Debe aparecer un enlace a los pedidos del cliente.
* Si el cliente tiene pedidos, normalmente no debe aparecer enlace/acción de borrado.

Ejemplo de elementos esperados en la respuesta:

"rel": "self"
"rel": "orders"

---

2. Comprobar enlaces hipermedia de un cliente sin pedidos

---

Comando:

curl.exe -i -H "Accept: application/json" http://localhost:7070/rs-orders-service/customers/2

Resultado esperado:

* Código HTTP 200 OK.
* La respuesta debe incluir enlace self.
* La respuesta puede incluir enlace a sus pedidos.
* Como el cliente 2 no tiene pedidos, puede incluir enlace/acción de borrado.

Ejemplo de elementos esperados:

"rel": "self"
"rel": "orders"
"rel": "delete"

---

3. Comprobar enlaces hipermedia de un pedido

---

Comando:

curl.exe -i -H "Accept: application/json" http://localhost:7070/rs-orders-service/orders/1

Resultado esperado:

* Código HTTP 200 OK.
* La respuesta del pedido debe incluir enlace self.
* La respuesta del pedido debe incluir enlace al cliente propietario.

Ejemplo de elementos esperados:

"rel": "self"
"rel": "customer"

---

4. Comprobar paginación hipermedia en pedidos de un cliente

---

Primera página, tamaño 1:

curl.exe -i -H "Accept: application/json" "http://localhost:7070/rs-orders-service/orders/customer/1?from=0&max=1"

Resultado esperado:

* Código HTTP 200 OK.
* Debe devolver solo el primer pedido del cliente 1.
* Si hay más pedidos, debe aparecer una cabecera Link con rel="next".

Ejemplo esperado en cabeceras:

Link: http://localhost:7070/rs-orders-service/orders/customer/1?from=1&max=1; rel="next"

Segunda página, tamaño 1:

curl.exe -i -H "Accept: application/json" "http://localhost:7070/rs-orders-service/orders/customer/1?from=1&max=1"

Resultado esperado:

* Código HTTP 200 OK.
* Debe devolver el segundo pedido del cliente 1.
* Debe aparecer una cabecera Link con rel="prev".
* Si no hay más pedidos, no tiene por qué aparecer rel="next".

Ejemplo esperado en cabeceras:

Link: http://localhost:7070/rs-orders-service/orders/customer/1?from=0&max=1; rel="prev"

---

5. Comprobar paginación hipermedia filtrando pedidos por estado

---

Comando:

curl.exe -i -H "Accept: application/json" "http://localhost:7070/rs-orders-service/orders/customer/1?status=PENDING&from=0&max=1"

Resultado esperado:

* Código HTTP 200 OK.
* Debe devolver como máximo un pedido del cliente 1 en estado PENDING.
* Si hay más resultados, debe aparecer cabecera Link con rel="next".
* Si no hay más resultados, no tiene por qué aparecer rel="next".

---

6. Comprobar paginación hipermedia en búsqueda de clientes

---

Comando:

curl.exe -i -H "Accept: application/json" "http://localhost:7070/rs-orders-service/customers?keywords=Cliente&from=0&max=1"

Resultado esperado:

* Código HTTP 200 OK.
* Debe devolver como máximo un cliente.
* Si hay más clientes que coinciden con la búsqueda, debe aparecer una cabecera Link con rel="next".

Segunda página:

curl.exe -i -H "Accept: application/json" "http://localhost:7070/rs-orders-service/customers?keywords=Cliente&from=1&max=1"

Resultado esperado:

* Código HTTP 200 OK.
* Debe aparecer una cabecera Link con rel="prev".
* Si no hay más clientes, no tiene por qué aparecer rel="next".

---

7. Variante XML de las mismas pruebas

---

Cliente con hipermedia en XML:

curl.exe -i -H "Accept: application/xml" http://localhost:7070/rs-orders-service/customers/1

Pedido con hipermedia en XML:

curl.exe -i -H "Accept: application/xml" http://localhost:7070/rs-orders-service/orders/1

Paginación XML de pedidos:

curl.exe -i -H "Accept: application/xml" "http://localhost:7070/rs-orders-service/orders/customer/1?from=0&max=1"

Resultado esperado:

* Código HTTP 200 OK.
* En el cuerpo XML deben aparecer los enlaces hipermedia si están implementados.
* En las búsquedas paginadas deben aparecer cabeceras Link con rel="prev" y/o rel="next" cuando corresponda.


============================================================



============================================================
PARTE BPEL / OPENESB / SOAPUI
============================================================

Requisitos previos:
- Tener instalado OpenESB Studio / OpenESB Standalone.
- Tener instalado SoapUI.
- Tener disponible el proyecto rs-orders-wscontrib.
- Tener abiertos en OpenESB los proyectos:
  - BPEL/PracticaBPEL
  - BPEL/IaCa

------------------------------------------------------------
1. Arrancar los servicios SOAP mock
------------------------------------------------------------

Desde el directorio del proyecto rs-orders-wscontrib:

cd rs-orders-wscontrib
mvn jetty:run

Comprobar que los WSDL están disponibles:

OrderService:
http://localhost:7070/rs-orders-wscontrib/services/OrderService?wsdl

InventoryService:
http://localhost:7070/rs-orders-wscontrib/services/InventoryService?wsdl

BillingService:
http://localhost:7070/rs-orders-wscontrib/services/BillingService?wsdl

ShippingService:
http://localhost:7070/rs-orders-wscontrib/services/ShippingService?wsdl


------------------------------------------------------------
2. Arrancar OpenESB Standalone
------------------------------------------------------------

Desde OpenESB Studio:

Services > Servers > OpenESB Standalone > Start

Comprobar que la consola web está disponible en:

http://localhost:4848/plugin/webui/#/assemblies

Usuario: admin
Password: admin


------------------------------------------------------------
3. Compilar y desplegar la Composite Application
------------------------------------------------------------

En OpenESB Studio:

1. Clic derecho sobre el proyecto BPEL/PracticaBPEL.
2. Clean and Build.

3. Clic derecho sobre el proyecto BPEL/IaCa.
4. Clean and Build.

5. Clic derecho sobre BPEL/IaCa.
6. Deploy.

En la consola web de OpenESB, la Service Assembly "IaCa" debe aparecer en estado STARTED.


------------------------------------------------------------
4. WSDL del proceso BPEL
------------------------------------------------------------

El WSDL del flujo BPEL está disponible en:

http://localhost:9080/OrderProcessService/OrderProcessPort?wsdl

El endpoint SOAP del flujo BPEL es:

http://localhost:9080/OrderProcessService/OrderProcessPort


------------------------------------------------------------
5. Proyectos SoapUI utilizados
------------------------------------------------------------

Se incluyen en el repositorio los proyectos SoapUI utilizados para las pruebas:

soapui/IaCa-BPEL-Test-soapui-project.xml
soapui/InventoryService-Test-soapui-project.xml

El proyecto "IaCa-BPEL-Test" contiene las peticiones:
- pedido correcto simple
- clienteincorrecto
- pedido sin stock
- notificar pedido

El proyecto "InventoryService-Test" contiene la petición:
- añadir stock


------------------------------------------------------------
6. Caso BPEL 1 - Procesar pedido sin añadir inventario
------------------------------------------------------------

Objetivo:
Procesar el pedido 1 del cliente 1, que tiene stock suficiente.

SoapUI:
Proyecto: IaCa-BPEL-Test
Operación: processOrder
Petición: pedido correcto simple

Entrada:

customerId = 1
orderId = 1

XML de la petición:

<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ord="http://j2ee.netbeans.org/wsdl/PracticaBPEL/src/OrderProcess">
   <soapenv:Header/>
   <soapenv:Body>
      <ord:processOrder>
         <customerId>1</customerId>
         <orderId>1</orderId>
      </ord:processOrder>
   </soapenv:Body>
</soapenv:Envelope>

Resultado esperado:
- Se obtiene el pedido 1.
- El pedido pertenece al cliente 1.
- El pedido cambia de PENDING a PROCESSING.
- InventoryService.checkInventory devuelve true.
- Se invoca ShippingService.shipOrder.
- Se invoca BillingService.createAndSendBill.
- El pedido cambia a PROCESSED.
- El flujo devuelve número de envío, número de factura, descripción e importe total de factura.

Valores esperados aproximados:
- orderDescription = 7 x1
- totalAmount = 24.14

Nota:
Los valores de shipmentId e invoiceId pueden variar si se han ejecutado pruebas anteriormente.


------------------------------------------------------------
7. Caso BPEL 2 - Procesar pedido con múltiples productos sin añadir inventario
------------------------------------------------------------

Objetivo:
Procesar el pedido 2 del cliente 1, que tiene varias líneas de pedido y stock suficiente.

SoapUI:
Proyecto: IaCa-BPEL-Test
Operación: processOrder

Entrada:

customerId = 1
orderId = 2

XML de la petición:

<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ord="http://j2ee.netbeans.org/wsdl/PracticaBPEL/src/OrderProcess">
   <soapenv:Header/>
   <soapenv:Body>
      <ord:processOrder>
         <customerId>1</customerId>
         <orderId>2</orderId>
      </ord:processOrder>
   </soapenv:Body>
</soapenv:Envelope>

Resultado esperado:
- Se obtiene el pedido 2.
- El pedido pertenece al cliente 1.
- El pedido cambia de PENDING a PROCESSING.
- Se recorren sus líneas de pedido.
- Se invoca InventoryService.checkInventory para cada línea.
- Se calcula el importe total del pedido.
- Se construye una descripción representativa.
- Se invocan BillingService y ShippingService.
- El pedido cambia a PROCESSED.

Pedido 2:
- productId = 1, quantity = 2, price = 10.55
- productId = 3, quantity = 1, price = 11.45
- productId = 5, quantity = 3, price = 4.95

Total sin IVA:
2 * 10.55 + 1 * 11.45 + 3 * 4.95 = 47.40

Total con IVA esperado:
47.40 * 1.21 = 57.35


------------------------------------------------------------
8. Caso BPEL 3 - Procesar pedido con modificación de inventario
------------------------------------------------------------

Objetivo:
Procesar el pedido 4 del cliente 1, que inicialmente no tiene stock suficiente.
El flujo debe quedarse esperando, se añade stock y después se notifica al BPEL.

Paso 1:
Lanzar el procesado del pedido.

SoapUI:
Proyecto: IaCa-BPEL-Test
Operación: processOrder
Petición: pedido sin stock

Entrada:

customerId = 1
orderId = 4

XML de la petición:

<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ord="http://j2ee.netbeans.org/wsdl/PracticaBPEL/src/OrderProcess">
   <soapenv:Header/>
   <soapenv:Body>
      <ord:processOrder>
         <customerId>1</customerId>
         <orderId>4</orderId>
      </ord:processOrder>
   </soapenv:Body>
</soapenv:Envelope>

Resultado esperado del paso 1:
- Se obtiene el pedido 4.
- El pedido pertenece al cliente 1.
- El pedido cambia de PENDING a PROCESSING.
- InventoryService.checkInventory devuelve false.
- El proceso BPEL queda esperando una notificación de stock.

Pedido 4:
- productId = 13
- quantity = 4
- price = 5.95


Paso 2:
Añadir stock al producto 13.

SoapUI:
Proyecto: InventoryService-Test
Operación: addInventory
Petición: añadir stock

Entrada:

productId = 13
quantity = 4

XML de la petición:

<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:inv="http://rs.udc.es/inventory">
   <soapenv:Header/>
   <soapenv:Body>
      <inv:addInventory>
         <productId>13</productId>
         <quantity>4</quantity>
      </inv:addInventory>
   </soapenv:Body>
</soapenv:Envelope>

Resultado esperado del paso 2:
- Se incrementa el stock del producto 13.


Paso 3:
Notificar al proceso BPEL que se ha modificado el inventario.

SoapUI:
Proyecto: IaCa-BPEL-Test
Operación: updateStockNotification
Petición: notificar pedido

Entrada:

orderId = 4

XML de la petición:

<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ord="http://j2ee.netbeans.org/wsdl/PracticaBPEL/src/OrderProcess">
   <soapenv:Header/>
   <soapenv:Body>
      <ord:updateStockNotification>
         <orderId>4</orderId>
      </ord:updateStockNotification>
   </soapenv:Body>
</soapenv:Envelope>

Resultado esperado del paso 3:
- El proceso BPEL se reanuda mediante correlación por orderId.
- Se vuelve a invocar InventoryService.checkInventory.
- Esta vez devuelve true.
- Se invoca BillingService.createAndSendBill.
- Se invoca ShippingService.shipOrder.
- El pedido cambia a PROCESSED.

Valores esperados aproximados:
- orderDescription = 13 x4
- totalAmount = 28.8


------------------------------------------------------------
9. Caso BPEL 4 - Procesar pedido con modificación de inventario y múltiples productos
------------------------------------------------------------

Objetivo:
Procesar el pedido 5 del cliente 1, que inicialmente no tiene stock suficiente en una de sus líneas.
El flujo debe quedarse esperando, se añade stock y se notifica al BPEL.

Paso 1:
Lanzar el procesado del pedido.

SoapUI:
Proyecto: IaCa-BPEL-Test
Operación: processOrder

Entrada:

customerId = 1
orderId = 5

XML de la petición:

<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ord="http://j2ee.netbeans.org/wsdl/PracticaBPEL/src/OrderProcess">
   <soapenv:Header/>
   <soapenv:Body>
      <ord:processOrder>
         <customerId>1</customerId>
         <orderId>5</orderId>
      </ord:processOrder>
   </soapenv:Body>
</soapenv:Envelope>

Resultado esperado:
- El proceso llega a InventoryService.checkInventory.
- Si no hay stock suficiente, queda esperando notificación.

Pedido 5:
- productId = 11, quantity = 4, price = 5.95
- productId = 9, quantity = 2, price = 5.95


Paso 2:
Añadir stock al producto 11.

SoapUI:
Proyecto: InventoryService-Test
Operación: addInventory

Entrada:

productId = 11
quantity = 4

XML de la petición:

<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:inv="http://rs.udc.es/inventory">
   <soapenv:Header/>
   <soapenv:Body>
      <inv:addInventory>
         <productId>11</productId>
         <quantity>4</quantity>
      </inv:addInventory>
   </soapenv:Body>
</soapenv:Envelope>


Paso 3:
Notificar al proceso BPEL.

SoapUI:
Proyecto: IaCa-BPEL-Test
Operación: updateStockNotification

Entrada:

orderId = 5

XML de la petición:

<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ord="http://j2ee.netbeans.org/wsdl/PracticaBPEL/src/OrderProcess">
   <soapenv:Header/>
   <soapenv:Body>
      <ord:updateStockNotification>
         <orderId>5</orderId>
      </ord:updateStockNotification>
   </soapenv:Body>
</soapenv:Envelope>

Resultado esperado:
- El proceso se reanuda.
- Vuelve a comprobar stock.
- Si hay stock suficiente en todas las líneas, continúa.
- Se invoca BillingService y ShippingService.
- El pedido cambia a PROCESSED.


------------------------------------------------------------
10. Caso BPEL 5 - Procesar pedido con cliente inválido
------------------------------------------------------------

Objetivo:
Intentar procesar el pedido 1 indicando un cliente incorrecto.

SoapUI:
Proyecto: IaCa-BPEL-Test
Operación: processOrder
Petición: clienteincorrecto

Entrada:

customerId = 2
orderId = 1

XML de la petición:

<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ord="http://j2ee.netbeans.org/wsdl/PracticaBPEL/src/OrderProcess">
   <soapenv:Header/>
   <soapenv:Body>
      <ord:processOrder>
         <customerId>2</customerId>
         <orderId>1</orderId>
      </ord:processOrder>
   </soapenv:Body>
</soapenv:Envelope>

Resultado esperado:
- Se obtiene el pedido 1.
- Se detecta que el pedido pertenece al cliente 1, no al cliente 2.
- El proceso devuelve un SOAP Fault InvalidCustomerFault.
- El flujo finaliza.
- No se cambia el estado del pedido.
- No se invoca InventoryService.
- No se invoca BillingService.
- No se invoca ShippingService.

Mensaje esperado:
CustomerId does not match the order customerId.


------------------------------------------------------------
11. Caso BPEL 6 - Procesar pedido ya tramitado
------------------------------------------------------------

Objetivo:
Intentar procesar de nuevo un pedido que ya ha sido tramitado.

Requisito previo:
Haber ejecutado antes el Caso BPEL 1, de forma que el pedido 1 ya esté en estado PROCESSED.

SoapUI:
Proyecto: IaCa-BPEL-Test
Operación: processOrder

Entrada:

customerId = 1
orderId = 1

XML de la petición:

<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ord="http://j2ee.netbeans.org/wsdl/PracticaBPEL/src/OrderProcess">
   <soapenv:Header/>
   <soapenv:Body>
      <ord:processOrder>
         <customerId>1</customerId>
         <orderId>1</orderId>
      </ord:processOrder>
   </soapenv:Body>
</soapenv:Envelope>

Resultado esperado:
- Se obtiene el pedido 1.
- El pedido ya está en estado PROCESSED.
- Al intentar cambiar su estado a PROCESSING, OrderService.changeStatus devuelve error.
- El flujo no continúa con inventario, facturación ni envío.


------------------------------------------------------------
12. Notas importantes sobre repetición de pruebas
------------------------------------------------------------

Los servicios mock mantienen estado en memoria.

Esto implica que:
- Los pedidos cambian de estado durante las pruebas.
- El inventario cambia al reservar o añadir unidades.
- Los identificadores de factura y envío se incrementan.

Para repetir las pruebas desde el estado inicial:
1. Parar rs-orders-wscontrib.
2. Volver a ejecutar mvn jetty:run.
3. En OpenESB, hacer undeploy/deploy de la Composite Application IaCa si es necesario.

También debe tenerse en cuenta que un pedido ya procesado no puede volver a procesarse, porque el servicio OrderService solo permite los cambios:
PENDING -> PROCESSING -> PROCESSED.
    mvn exec:java -Dexec.mainClass="es.udc.rs.orders.client.ui.OrderServiceClient" -Dexec.args="-findCustomer 1"

