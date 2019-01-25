# The Challenge:

The challenge is to create a simple Android app that exercises a REST-ful API. The API endpoint `http://api.nytimes.com/svc/movies/v2/reviews/dvd-picks.json?order=by-date&api-key=KoRB4K5LRHygfjCL2AH6iQ7NeUqDAGAB&offset=0` returns a JSON object which is a list of different movie reviews published by the New York Times. 
For example:
```json
  "results": [
    {
      "display_title": "Rat Film",
      "mpaa_rating": "",
      "headline": "Review: In \u2018Rat Film,\u2019 a City of Rodents and Racial Oppression",
      "multimedia": {
        "type": "mediumThreeByTwo210",
        "src": "https:\/\/static01.nyt.com\/images\/2018\/09\/19\/arts\/19colette1\/merlin_143890539_b04f8a07-099a-46e2-9af4-3de2e7185111-mediumThreeByTwo210.jpg",
        "width": 210,
        "height": 140
      }
    },
    {
      "display_title": "Woodpeckers",
      "mpaa_rating": "",
      "headline": "Review: \u2018Woodpeckers,\u2019 a Tale of Love and Agonizing Penal Confinement",
      ....
    },
    ..
    ]
```

Using this endpoint, show a list of these items, with each row displaying at least the following fields:
- `display_title` (movie title)
- `mpaa_rating` (movie rating)
- `byline` (reviewer)
- `headline` 
- `summary_short`
- `publication_date`
- `multimedia` 

Feel free to make any assumptions you want along the way or use any third party libraries as needed (just document them in a your pull request).

### General Instructions:
- This isn't a visual design exercise. For example, if you set random background colors to clearly differentiate the views when debugging, pick Comic Sans or Papyrus, we won't hold that against you. Well, maybe a little bit if you use Comic Sans :)
- This is also most of the code you'll be showing us – don't understimate the difficulty of the task, or the importance of this exercise in our process, and rush your PR. Put up your best professional game.
- This isn't just about handling the happy path. Think slow network (or no network at all), supporting different device sizes, ease of build and run of the project. If we can't check out and click the run button in Android Studio, you're off to a bad start (we've had PRs without a graddle for instance).
- "Surprise us by writing the most boring and unambiguous code possible". We are more interested in stability/maintainability of your code than in "show off" features. We'd much rather know how you'd handle errors hollistically, or dates and their timezones, than seeing how you'd implement parallax scrolling with sparkles and flames for instance.
- Tests (unit or UI) are welcome, but not required.
- Explanations on any choice you've made are welcome.
- We appreciate there's a lot that is asked in this exercise. If you need more time, feel free to ask. If you need to de-prioritize something, apply the same judgement you would on a professional project, argument your decision.
