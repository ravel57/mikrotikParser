[Front-end:](https://github.com/ravel57/mikrotik_parser)

Запуск Docker:
-
``` 
sudo docker build -t mikrotik_parser . && sudo docker run --name mikrotik_parser -d --restart unless-stopped -p 9095:9095 --env GATEWAY=<YOUER_MIKROTIK_IP> --env MIKROTIK_PASSWORD=<MIKROTIK_PASSWORD> --env MIKROTIK_USER=<MIKROTIK_USER> mikrotik_parser
```
