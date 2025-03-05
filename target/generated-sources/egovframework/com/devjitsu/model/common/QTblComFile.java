package egovframework.com.devjitsu.model.common;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QTblComFile is a Querydsl query type for TblComFile
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTblComFile extends EntityPathBase<TblComFile> {

    private static final long serialVersionUID = 1108029399L;

    public static final QTblComFile tblComFile = new QTblComFile("tblComFile");

    public final StringPath actvtnYn = createString("actvtnYn");

    public final StringPath atchFileExtnNm = createString("atchFileExtnNm");

    public final StringPath atchFileNm = createString("atchFileNm");

    public final StringPath atchFilePathNm = createString("atchFilePathNm");

    public final NumberPath<Long> atchFileSn = createNumber("atchFileSn", Long.class);

    public final NumberPath<Long> atchFileSz = createNumber("atchFileSz", Long.class);

    public final NumberPath<Long> creatrSn = createNumber("creatrSn", Long.class);

    public final DateTimePath<java.time.LocalDateTime> frstCrtDt = createDateTime("frstCrtDt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> mdfcnDt = createDateTime("mdfcnDt", java.time.LocalDateTime.class);

    public final NumberPath<Long> mdfrSn = createNumber("mdfrSn", Long.class);

    public final StringPath psnTblSn = createString("psnTblSn");

    public final StringPath strgFileNm = createString("strgFileNm");

    public QTblComFile(String variable) {
        super(TblComFile.class, forVariable(variable));
    }

    public QTblComFile(Path<? extends TblComFile> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTblComFile(PathMetadata metadata) {
        super(TblComFile.class, metadata);
    }

}

