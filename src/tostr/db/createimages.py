from pylab import imshow, show, get_cmap, savefig
from numpy import _random

def randToast(n):
    # I adapted this from StackOverflow
    Z = random.random((50,50))
    imshow(Z, cmap=get_cmap("Spectral"), interpolation="nearest")
    savefig(n+".csv")
