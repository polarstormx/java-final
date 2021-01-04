package interfaces;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface IsCharacter {
	String name();

	//  
	String side();

	int HP();

	int MP();

	int damageR();

	int damageAOE();

	int damageZXC();

	boolean isRemote();

	boolean canCure();
}