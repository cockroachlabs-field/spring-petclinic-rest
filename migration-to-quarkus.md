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

## TODO

1. Security in Controllers :  @PreAuthorize( "hasRole(@roles.OWNER_ADMIN)" )
2. Transactional
3. JDBC repositories
4. CROSS Origin 
  