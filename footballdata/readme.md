# Football Data

## How to 

I have added two sbt aliases:

`sbt commitCheck`: `clean; compile; test`
`sbt rerun`: `clean; run`

## Client

To hit the football api client, manually pass api key into `keyHeader`.

Of course, my personal key remains private.

## Endpoints

`/status` - football api status endpoint
`/teamTransfers/{teamId}` - get transfers by team

## Interesting teams

```json
"team_id": 64,
"name": "Hull City",
"code": null,
"logo": "https://media.api-sports.io/football/teams/64.png",
"country": "England",
"founded": 1904,
"venue_name": "KCOM Stadium",
"venue_surface": "grass",
"venue_address": "Walton Street / Anlaby Road",
"venue_capacity": 25504


"team_id": 40,
"name": "Liverpool",
"code": null,
"logo": "https://media.api-sports.io/football/teams/40.png",
"country": "England",
"venue_name": "Anfield",
"venue_surface": "grass",
"venue_city": "Liverpool",
"venue_capacity": 55212
```