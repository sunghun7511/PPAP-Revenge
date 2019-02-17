package kr.kshgroup.ppap_revenge.files;

import java.io.File;

import kr.kshgroup.ppap_revenge.ManagerBase;

public interface FileManagerBase<T> extends ManagerBase {

	public abstract T readFile(String filename);

	public abstract T readFile(File file);

	public abstract void writeFile(String filename, T data);

	public abstract void writeFile(File file, T data);

	public abstract void appendFile(String filename, T data);

	public abstract void appendFile(File file, T data);

}
