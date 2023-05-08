/*
	カレントディレクトリの配下にあるリリースされたファイルを回収(コピー)する。
*/

#include "C:\Factory\Common\all.h"

static int IsReleasedFile(char *file)
{
	autoList_t *pTkns = tokenizeYen(file);
	int ret = 0;

	if (3 <= getCount(pTkns))
	{
		char *dirName  = getLine(pTkns, getCount(pTkns) - 2);
		char *fileName = getLine(pTkns, getCount(pTkns) - 1);

		if (!_stricmp(dirName, "out") && endsWithICase(fileName, ".zip"))
		{
			ret = 1;
		}
	}
	releaseDim(pTkns, 1);
	return ret;
}
static void CollectOut(void)
{
	autoList_t *files = lssFiles(".");
	char *file;
	uint index;
	char *outDir = makeFreeDir();

	foreach (files, file, index)
	if (IsReleasedFile(file))
	{
		char *outFile = toCreatablePath(combine(outDir, getLocal(file)), index + 10); // index + margin

		cout("< %s\n", file);
		cout("> %s\n", outFile);

		copyFile(file, outFile);
		memFree(outFile);
	}
	releaseDim(files, 1);
	execute_x(xcout("START %s", outDir));
	memFree(outDir);
}
int main(int argc, char **argv)
{
	CollectOut();
}
