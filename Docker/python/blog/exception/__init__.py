from os.path import dirname, basename, isfile, join
import glob

"""
 Metodo per prendere tutti i file di una cartella .py e filtrarli al
 fine di esportare solo i moduli, escludendo il .py
"""
modules = glob.glob(join(dirname(__file__), "*.py"))

__all__ = [ basename(f)[:-3] for f in modules if isfile(f) and not f.endswith('__init__.py')]