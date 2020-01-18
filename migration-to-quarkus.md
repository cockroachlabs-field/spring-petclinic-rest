# Migration of Spring Pet Clinic to Quarkus

## Libraries used

1. Spring
   1. actuator
   2. cache
   3. data-jpa
   4. web
   5. test
   6. devtools
2. hsqldb
3. mysql
4. cache-api
5. ehcache


## Steps

1. Add Quarkus dependencies
2. Replaced PropertyComparator and MutableSortDefinition by a Java Stream
3. Replace Autowired by Inject
4. Removed @Profile and @Repository
5. Repository interfaces implement PanacheRepository
6. Rename findAll to listAll on repositories
7. Changed return type for listAll
8. Removed SpringApplication
9. Changing @RestController, @RequestMapping, @PathVariable -> @Path , @GET,@POST,@PUT,@DELETE, @PathParam, @Produces
10. Changing ResponseEntity by Response.entity
11. Changing BindingResult by ConstraintValidation
12. Removed JDBC repositories
13. Addded ApplicationScoped to repository implemented class
14. Added REST Security through @RolesAllowed
15. Replaced hard coded roles with Roles constants
16. CORS configured on application.properties file
17. Removed DataAccessException throw clause
18. Removed 2 levels repository implementation *Repository + * RepositoryImpl -> *Repository
19. Moved to JUnit 5
20. Removed in tests : SpringBootTest , ContextConfiguration, WebAppConfiguration
21. Added QuarkusTest
22. Removed tests classes for JDBC and SpringDataJPA
23. Migrated REST tests to RestAssured and Junit 5
24. Added OpenAPI support and removed ApplicationSwaggerConfig
25. Removed security classes as we do with Elytron conf
26. Added properties for security
27. Added microprofile metrics with smallrye-metrics extension
28. Modified tests to use QuarkusTest, removing Mocking and removing the abstract test class
29. Migrated @ControllerAdvice as global exception handler to JAX-RS ExceptionMapper
30. Converted PropertyComparator and MutableSortDefinition to Stream.sorted

## TODO

1. Cache
2. Test
3. AspectJ for monitoring calls
4. ControllerAdvice
5. DB Initial Schema
  
## NOT MIGRATED

1. JDBC repositories
2. Spring Data repositories