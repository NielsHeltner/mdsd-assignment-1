# Model-driven Software Development assignment 1
Internal DSL in Java for defining a system of microservices.

The project contains the four following packages.
## mdsd.model
This package contains classes that represent the meta model.

`MetaModel` contains a collection of `Microservice`s in the system. Each `Microservice` has, among other attributes, a collection of `Endpoint`s. Each `Endpoint` consists, among other attributes, of a valid `HttpMethod` and a collection of valid parameters, as well as an `Invocable` for storing a closure for deferred invocation.

## mdsd.dsl.internal
This package contains a single class `MicroserviceBuilder`, which is implemented based on the expression builder pattern [Fowler]. The class defines the domain-specific language, and acts as a fluent interface for building a `MetaModel`.

## mdsd.executors
This package contains classes for executing a configured meta model.

The `MetaModelExecutor` takes a `MetaModel` as argument, and executes it by instantiating a `HttpSocketMicroserviceExecutor` for each `Microservice` in the model.

The `HttpSocketMicroserviceExecutor` executes the `Microservice` model by exposing it on a network and listening to a port. It is implemented using `Socket`s for communication, and also features threading for handling incoming requests.

The `MetaModelTester` class contains testing functionality for simulating a client interacting with the executing `Microservice`s over a network.

## mdsd.instances.cinema
This package contains classes that use the internal DSL and populates the meta model.

The class `CinemaScript` uses the internal DSL to populate the meta model, while the class `Cinema` acts as a main class. It invokes the `MetaModelExecutor`, passing it the populated meta model. It then invokes the methods of the `MetaModelTester` to showcase usecases of the system, and how it behaves.

## Additional notes
The project uses Maven for dependencies, and should be built with `mvn package` or similar.
