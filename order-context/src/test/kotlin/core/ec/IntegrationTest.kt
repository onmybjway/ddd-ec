package core.ec

import org.springframework.test.context.ActiveProfiles
import java.lang.annotation.ElementType


@Target(AnnotationTarget.CLASS)
@ActiveProfiles("integration")
annotation class IntegrationTest