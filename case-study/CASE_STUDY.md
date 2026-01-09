# Case Study Scenarios to discuss

## Scenario 1: Cost Allocation and Tracking
**Situation**: The company needs to track and allocate costs accurately across different Warehouses and Stores. The costs include labor, inventory, transportation, and overhead expenses.

**Task**: Discuss the challenges in accurately tracking and allocating costs in a fulfillment environment. Think about what are important considerations for this, what are previous experiences that you have you could related to this problem and elaborate some questions and considerations

**Questions you may have and considerations:**
Challenges in Accurate Cost Tracking & Allocation

Accurately tracking and allocating costs in a fulfillment environment is challenging because costs are multi-dimensional and often shared across entities.

Architecture / Implementation Thoughts

Introduce a CostEntry or CostAllocation domain entity.

Use REST endpoints like:

POST /warehouses/{id}/costs
GET /warehouses/{id}/costs?from=&to=


Use immutable cost records (append-only) for auditing.

Maintain warehouse lifecycle states (ACTIVE, ARCHIVED).

## Scenario 2: Cost Optimization Strategies
**Situation**: The company wants to identify and implement cost optimization strategies for its fulfillment operations. The goal is to reduce overall costs without compromising service quality.

**Task**: Discuss potential cost optimization strategies for fulfillment operations and expected outcomes from that. How would you identify, prioritize and implement these strategies?

**Questions you may have and considerations:**
System-driven strategies:

Identify underperforming warehouses using KPIs.

Compare cost-per-unit across Warehouses and Stores.

Detect cost anomalies automatically.

Java / Spring Considerations

Expose analytics endpoints:

GET /warehouses/{id}/cost-metrics
GET /locations/{id}/cost-comparison


Asynchronous processing for heavy analytics (Spring Events / Messaging).

Feature flags to roll out optimizations safely.

## Scenario 3: Integration with Financial Systems
**Situation**: The Cost Control Tool needs to integrate with existing financial systems to ensure accurate and timely cost data. The integration should support real-time data synchronization and reporting.

**Task**: Discuss the importance of integrating the Cost Control Tool with financial systems. What benefits the company would have from that and how would you ensure seamless integration and data synchronization?

**Questions you may have and considerations:**
Technical approaches:

REST APIs for synchronous operations.

Event-driven integration (e.g., cost posted, warehouse archived).

Scheduled reconciliation jobs.

Idempotent endpoints for financial postings.

Design principles:

Clear contract definitions (OpenAPI).

Mapping between operational IDs and financial cost centers.

Strong validation and error handling.

Monitoring and alerting for integration failures.

## Scenario 4: Budgeting and Forecasting
**Situation**: The company needs to develop budgeting and forecasting capabilities for its fulfillment operations. The goal is to predict future costs and allocate resources effectively.

**Task**: Discuss the importance of budgeting and forecasting in fulfillment operations and what would you take into account designing a system to support accurate budgeting and forecasting?

**Questions you may have and considerations:**
Design Considerations for Accurate Forecasting

Inputs to consider:

Historical cost trends.

Seasonality and demand forecasts.

Warehouse lifecycle events (open, replace, archive).

Inflation, labor cost changes, supplier contracts.

System Capabilities

Budget versions (baseline, revised).

Forecast vs actual comparisons.

Scenario modeling (what-if analysis).

Time-based projections (monthly, quarterly).

Spring Boot / API Considerations

Forecast endpoints:

POST /warehouses/{id}/forecast
GET /warehouses/{id}/forecast-vs-actual


Separate forecasting models from transactional data.

Keep forecasts immutable once approved.

## Scenario 5: Cost Control in Warehouse Replacement
**Situation**: The company is planning to replace an existing Warehouse with a new one. The new Warehouse will reuse the Business Unit Code of the old Warehouse. The old Warehouse will be archived, but its cost history must be preserved.

**Task**: Discuss the cost control aspects of replacing a Warehouse. Why is it important to preserve cost history and how this relates to keeping the new Warehouse operation within budget?

**Questions you may have and considerations:**
Relationship to Budget Control

Historical costs define baseline expectations.

New Warehouse budgets should reference archived data.

Cost overruns can be detected earlier using historical benchmarks.

Replacement costs (one-time vs recurring) must be separated.

System Design Considerations

Keep Business Unit Code stable, but version Warehouses internally.

Link archived and active Warehouses via lineage.

Maintain immutable cost records tied to Warehouse versions.

Support APIs like:

POST /warehouses/{id}/replace
GET /warehouses/{businessUnitCode}/cost-history

## Instructions for Candidates
Before starting the case study, read the [BRIEFING.md](BRIEFING.md) to quickly understand the domain, entities, business rules, and other relevant details.

**Analyze the Scenarios**: Carefully analyze each scenario and consider the tasks provided. To make informed decisions about the project's scope and ensure valuable outcomes, what key information would you seek to gather before defining the boundaries of the work? Your goal is to bridge technical aspects with business value, bringing a high level discussion; no need to deep dive.
