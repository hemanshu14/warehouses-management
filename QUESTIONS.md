# Questions

Here we have 3 questions related to the code base for you to answer. It is not about right or wrong, but more about what's the reasoning behind your decisions.

1. In this code base, we have some different implementation strategies when it comes to database access layer and manipulation. If you would maintain this code base, would you refactor any of those? Why?

**Answer:**
```
I have worked mostly with JPARepository instead of PanacheRepository but what I have understood so far is that JPARepository should be used if we have to implement complex dynamic queries like Advanced Joins and we need full control of full JPA for performance tuning. 
Panache has less boilerplate, clear query API like find("field = ?1", val) or findById() and built-in pagination which require less code and less chance of mistakes.

```
----
2. When it comes to API spec and endpoints handlers, we have an Open API yaml file for the `Warehouse` API from which we generate code, but for the other endpoints - `Product` and `Store` - we just coded directly everything. What would be your thoughts about what are the pros and cons of each approach and what would be your choice?

**Answer:**
```
Generating models from the Open API yaml file is definitely a good idea because then we promote design API first approach which is the biggest advantage as there will be clear API contracts and no accidental changes can happen. It also reduces the drift between code and documentation.
Also, it requires discipline where generated code must be write-only and never should be manually editable. It could also be hard in the scenarios where a lot of custom validations are required, there generating models from open api yaml file could be a lot of work.

In general, I would have named the endpoints resource name as "warehouses" according to the REST design standards and must have done some versioning as well for the REST endpoints.
``` 
