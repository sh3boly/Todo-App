typealias TodosRoot = List<TodoModel>;

data class TodoModel(
    val userId: Long,
    val id: Long,
    val title: String,
    val completed: Boolean,
)
