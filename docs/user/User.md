## User

### loadUser()

```text
In -> user.id 
Out -> User object
```

### updateUser()

```text
In -> user.id, User object
Out -> Nothing

Updating only the attributes that came in request.
If request contains addresses it will update them 
or add them if the address type does not exist for that User
```