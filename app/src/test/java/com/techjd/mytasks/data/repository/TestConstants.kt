package com.techjd.mytasks.data.repository

object TestConstants {

  val tasksJson = """
    {
        "tasks": [
            {
                "task_id": 2273,
                "task_detail": {
                    "date": 1723766400,
                    "title": "DC dvfbgn",
                    "description": "thtngn"
                }
            },
            {
                "task_id": 2275,
                "task_detail": {
                    "date": 1723766400,
                    "title": "rbfbfb",
                    "description": "👀"
                }
            },
            {
                "task_id": 2269,
                "task_detail": {
                    "date": 1721347200,
                    "title": "Hello World",
                    "description": "Qwerty"
                }
            },
            {
                "task_id": 2270,
                "task_detail": {
                    "date": 1721260800,
                    "title": "jdjdjd",
                    "description": "kfkfjf"
                }
            }
        ]
    }
  """.trimIndent()

  val successJson = """
    {
        "status": "Success"
    }
  """.trimIndent()
}
