import com.github.kantis.mikrom.ParameterMapper
import com.github.kantis.mikrom.Query

public object QueryExecution {

    public fun <T> executeWithParameters(
        query: Query,
        value: T,
        parameterMapper: ParameterMapper<T>
    ): Map<String, Any?> {
        return parameterMapper.map(value)
    }
}
