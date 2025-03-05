package egovframework.com.devjitsu.model.menu;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QTblMenu is a Querydsl query type for TblMenu
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTblMenu extends EntityPathBase<TblMenu> {

    private static final long serialVersionUID = -1404514151L;

    public static final QTblMenu tblMenu = new QTblMenu("tblMenu");

    public final StringPath actvtnYn = createString("actvtnYn");

    public final StringPath aplcnNtnLtr = createString("aplcnNtnLtr");

    public final NumberPath<Long> bbsSn = createNumber("bbsSn", Long.class);

    public final NumberPath<Long> creatrSn = createNumber("creatrSn", Long.class);

    public final DateTimePath<java.time.LocalDateTime> frstCrtDt = createDateTime("frstCrtDt", java.time.LocalDateTime.class);

    public final StringPath lwrMenuEn = createString("lwrMenuEn");

    public final DateTimePath<java.time.LocalDateTime> mdfcnDt = createDateTime("mdfcnDt", java.time.LocalDateTime.class);

    public final NumberPath<Long> mdfrSn = createNumber("mdfrSn", Long.class);

    public final StringPath menuNm = createString("menuNm");

    public final StringPath menuNmPath = createString("menuNmPath");

    public final StringPath menuPathNm = createString("menuPathNm");

    public final NumberPath<Long> menuSeq = createNumber("menuSeq", Long.class);

    public final NumberPath<Long> menuSn = createNumber("menuSn", Long.class);

    public final StringPath menuSnPath = createString("menuSnPath");

    public final NumberPath<Long> menuSortSeq = createNumber("menuSortSeq", Long.class);

    public final StringPath menuType = createString("menuType");

    public final StringPath menuWholPath = createString("menuWholPath");

    public final NumberPath<Long> upperMenuSn = createNumber("upperMenuSn", Long.class);

    public QTblMenu(String variable) {
        super(TblMenu.class, forVariable(variable));
    }

    public QTblMenu(Path<? extends TblMenu> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTblMenu(PathMetadata metadata) {
        super(TblMenu.class, metadata);
    }

}

