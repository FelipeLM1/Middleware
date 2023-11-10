# Middleware
Projeto para a disciplina de Sistemas Distribuídos

## Patterns

### Basic Remoting 
- [x] Server Request Handler
- [x] Client Request Handler (Não integrado MQTT)
- [x] Remoting Error
- [x] Invoker (Não integrado MQTT)
- [x] Requestor
- [x] Marshaller (Não integrado MQTT)
- [x] Remote Object (Não integrado MQTT)

### Idetification
- [x] Object ID
- [x] Absolute Object Reference
- [x] Lookup?

### Lifecycle Management
- [x] Static Instance
- [x] Per-Request Instance
- [x] Lazy Acquisition
- [x] Eager Acquisition

### Extension
- [ ] Invocation Interceptor
- [ ] Invocation Context
- [ ] Protocol Plug-In

## Aplications

### S3 (Falta desacoplar do middleware)
- [X] POST
- [X] GET
- [ ] UPLOAD FILE
- [ ] DOWNLOAD FILE

### MQTT (Não intergrado com o Middleware)
- [X] Client
- [X] Broker
