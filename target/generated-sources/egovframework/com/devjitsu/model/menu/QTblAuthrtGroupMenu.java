package egovframework.com.devjitsu.model.menu;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QTblAuthrtGroupMenu is a Querydsl query type for TblAuthrtGroupMenu
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTblAuthrtGroupMenu extends EntityPathBase<TblAuthrtGroupMenu> {

    private static final long serialVersionUID = 2131397178L;

    public static final QTblAuthrtGroupMenu tblAuthrtGroupMenu = new QTblAuthrtGroupMenu("tblAuthrtGroupMenu");

    public final NumberPath<Long> authrtGroupMenuSn = createNumber("authrtGroupMenuSn", Long.class);

    public final NumberPath<Long> authrtGroupSn = createNumber("authrtGroupSn", Long.class);

    public final NumberPath<Long> creatrSn = createNumber("creatrSn", Long.class);

    public final DateTimePath<java.time.LocalDateTime> frstCrtDt = createDateTime("frstCrtDt", java.time.LocalDateTime.class);

    public final NumberPath<Long> menuSn = createNumber("menuSn", Long.class);

    public QTblAuthrtGroupMenu(String variable) {
        super(TblAuthrtGroupMenu.class, forVariable(variable));
    }

    public QTblAuthrtGroupMenu(Path<? extends TblAuthrtGroupMenu> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTblAuthrtGroupMenu(PathMetadata metadata) {
        super(TblAuthrtGroupMenu.class, metadata);
    }

}

