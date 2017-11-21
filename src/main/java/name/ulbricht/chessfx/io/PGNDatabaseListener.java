package name.ulbricht.chessfx.io;

import org.antlr.v4.runtime.tree.ParseTreeListener;

interface PGNDatabaseListener<T> extends ParseTreeListener {

    T getData();
}
