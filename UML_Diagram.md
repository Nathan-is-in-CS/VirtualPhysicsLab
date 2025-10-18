# Virtual Physics Lab – UML Class Diagram

```mermaid
classDiagram
    %% Main Controller
    class VirtualPhysicsLab {
        +main(String[] args)
        -scanner : Scanner
        -experiment : Experiment
    }

    %% Abstract / Base Class
    class Experiment {
        <<abstract>>
        -name : String
        -difficulty : DifficultyLevel
        +getName() String
        +getDifficulty() DifficultyLevel
        +runExperiment() void
    }

    %% Subclasses
    class CircuitExperiment {
        -voltage : double
        -current : double
        -resistance : double
        +runExperiment() void
    }

    class OpticsExperiment {
        -focalLength : double
        -objectDistance : double
        -imageDistance : double
        +runExperiment() void
    }

    class ProjectileTesting {
        -velocity : double
        -angle : double
        -time : double
        -distance : double
        +runExperiment() void
    }

    %% Enums
    class DifficultyLevel {
        <<enumeration>>
        EASY
        MEDIUM
        HARD
    }

    class ExperimentType {
        <<enumeration>>
        CIRCUIT
        OPTICS
        PROJECTILE
    }

    %% Exceptions
    class ExperimentException {
        <<exception>>
    }

    class MaxAttemptsExceededException {
        <<exception>>
    }

    class TimeExpiredException {
        <<exception>>
    }

    %% Relationships
    Experiment <|-- CircuitExperiment
    Experiment <|-- OpticsExperiment
    Experiment <|-- ProjectileTesting

    VirtualPhysicsLab --> ExperimentType
    VirtualPhysicsLab --> Experiment

    Experiment --> DifficultyLevel

    ExperimentException <|-- MaxAttemptsExceededException
    ExperimentException <|-- TimeExpiredException
